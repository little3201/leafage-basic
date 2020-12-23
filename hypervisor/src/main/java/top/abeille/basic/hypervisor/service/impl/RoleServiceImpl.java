/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.Role;
import top.abeille.basic.hypervisor.document.RoleAuthority;
import top.abeille.basic.hypervisor.dto.RoleAuthorityDTO;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.repository.AuthorityRepository;
import top.abeille.basic.hypervisor.repository.RoleAuthorityRepository;
import top.abeille.basic.hypervisor.repository.RoleRepository;
import top.abeille.basic.hypervisor.service.RoleService;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 角色信息service 实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends AbstractBasicService implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleAuthorityRepository roleAuthorityRepository,
                           AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<RoleVO> retrieve(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modify_time");
        return roleRepository.findByEnabledTrue(PageRequest.of(page, size, sort)).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> fetch(String code) {
        return roleRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
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

    private Mono<List<RoleAuthority>> initRoleAuthority(String roleId, String modifier,
                                                        Collection<RoleAuthorityDTO> roleAuthorityDTOs) {
        return Mono.just(roleAuthorityDTOs).flatMap(roleAuthorityDTOS -> {
            List<RoleAuthority> roleAuthorities = new ArrayList<>(roleAuthorityDTOs.size());
            roleAuthorityDTOS.forEach(roleAuthorityDTO ->
                    authorityRepository.findByCodeAndEnabledTrue(roleAuthorityDTO.getCode())
                            .switchIfEmpty(Mono.error(NotContextException::new))
                            .doOnNext(authority -> {
                                RoleAuthority roleAuthority = new RoleAuthority();
                                roleAuthority.setRoleId(roleId);
                                roleAuthority.setAuthorityId(authority.getId());
                                roleAuthority.setMode(roleAuthorityDTO.getMode());
                                roleAuthority.setModifier(modifier);
                                roleAuthorities.add(roleAuthority);
                            }));
            return Mono.just(roleAuthorities);
        });
    }

}
