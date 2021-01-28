/*
 * Copyright (c) 2019. Abeille All Right Reserved.
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
import io.leafage.basic.hypervisor.vo.CountVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.common.basic.AbstractBasicService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色信息service 实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends AbstractBasicService implements RoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public RoleServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository, RoleAuthorityRepository roleAuthorityRepository,
                           AuthorityRepository authorityRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<RoleVO> retrieve(int page, int size) {
        return roleRepository.findByEnabledTrue(PageRequest.of(page, size))
                .map(this::convertOuter);
    }

    @Override
    public Flux<CountVO> countRelations(Set<String> ids) {
        return Flux.fromIterable(ids).flatMap(roleId ->
                userRoleRepository.countByRoleIdAndEnabledTrue(roleId).map(count -> {
                    CountVO countVO = new CountVO();
                    countVO.setId(roleId);
                    countVO.setCount(count);
                    return countVO;
                })
        );
    }

    @Override
    public Mono<RoleVO> fetch(String code) {
        return roleRepository.findByCodeAndEnabledTrue(code)
                .map(this::convertOuter);
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
        Mono<Role> roleMono = roleRepository.findByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(role -> {
                    BeanUtils.copyProperties(roleDTO, role);
                    return roleRepository.save(role)
                            .switchIfEmpty(Mono.error(RuntimeException::new))
                            .doOnSuccess(r ->
                                    this.initRoleAuthority(r.getId(), r.getModifier(), roleDTO.getAuthorities())
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
    private Mono<List<RoleAuthority>> initRoleAuthority(String roleId, String modifier,
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
