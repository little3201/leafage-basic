/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleAuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.Collection;
import java.util.List;

/**
 * 角色信息service 接口实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends AbstractBasicService implements RoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public RoleServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository,
                           RoleAuthorityRepository roleAuthorityRepository, AuthorityRepository authorityRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<RoleVO> retrieve() {
        return roleRepository.findAll().map(this::convertOuter);
    }

    @Override
    public Flux<RoleVO> retrieve(int page, int size) {
        return roleRepository.findByEnabledTrue(PageRequest.of(page, size))
                .flatMap(role -> userRoleRepository.countByRoleIdAndEnabledTrue(role.getId()).map(count -> {
                            RoleVO roleVO = new RoleVO();
                            BeanUtils.copyProperties(role, roleVO);
                            roleVO.setCount(count);
                            return roleVO;
                        })
                );
    }

    @Override
    public Mono<RoleVO> fetch(String code) {
        Assert.hasText(code, "code is blank");
        return roleRepository.getByCodeAndEnabledTrue(code)
                .flatMap(role -> userRoleRepository.countByRoleIdAndEnabledTrue(role.getId()).map(count -> {
                            RoleVO roleVO = new RoleVO();
                            BeanUtils.copyProperties(role, roleVO);
                            roleVO.setCount(count);
                            return roleVO;
                        })
                );
    }

    @Override
    public Mono<Long> count() {
        return roleRepository.count();
    }

    @Override
    public Mono<RoleVO> create(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setCode(this.generateCode());
        Mono<Role> roleMono = roleRepository.insert(role)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .doOnSuccess(r -> this.initRoleAuthority(r.getId(), r.getModifier(), roleDTO.getAuthorities())
                        .subscribe(roleAuthorities -> roleAuthorityRepository.saveAll(roleAuthorities).subscribe()));
        return roleMono.map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(String code, RoleDTO roleDTO) {
        Assert.hasText(code, "code is blank");
        Mono<Role> roleMono = roleRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(role -> {
                    BeanUtils.copyProperties(roleDTO, role);
                    return roleRepository.save(role)
                            .switchIfEmpty(Mono.error(RuntimeException::new))
                            .doOnSuccess(r -> this.initRoleAuthority(r.getId(), r.getModifier(), roleDTO.getAuthorities())
                                    .subscribe(roleAuthorities -> roleAuthorityRepository.saveAll(roleAuthorities).subscribe()));
                });
        return roleMono.map(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private RoleVO convertOuter(Role info) {
        RoleVO outer = new RoleVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

    /**
     * 构造 roleAuthority
     *
     * @param roleId      角色ID
     * @param modifier    修改人
     * @param authorities 权限code
     * @return 角色权限对象
     */
    private Mono<List<RoleAuthority>> initRoleAuthority(ObjectId roleId, ObjectId modifier,
                                                        Collection<String> authorities) {
        return authorityRepository.findByCodeInAndEnabledTrue(authorities).map(authority -> {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(roleId);
            roleAuthority.setAuthorityId(authority.getId());
            roleAuthority.setModifier(modifier);
            return roleAuthority;
        }).collectList();
    }

}
