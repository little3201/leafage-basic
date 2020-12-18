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
import top.abeille.basic.hypervisor.document.Authority;
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
import java.util.List;
import java.util.stream.Collectors;

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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
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
        Mono<Role> roleMono = roleRepository.insert(role);
        // 处理权限
        roleMono.map(r -> {
            List<Mono<Authority>> monoList = roleDTO.getAuthorities().stream().map(authorityDTO ->
                    this.initRoleAuthority(r.getId(), r.getModifier(), authorityDTO)
                            .switchIfEmpty(Mono.empty()))
                    .collect(Collectors.toList());
            return monoList.stream().sequential().map(authorityMono -> authorityMono.map(authorityRepository::save));
        });
        return roleMono.map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(String code, RoleDTO roleDTO) {
        Mono<Role> roleMono = roleRepository.findByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(role -> {
                    BeanUtils.copyProperties(roleDTO, role);
                    return roleRepository.save(role);
                });
        // 处理权限
        roleMono.map(role -> {
            List<Mono<Authority>> monoList = roleDTO.getAuthorities().stream().map(authorityDTO ->
                    this.initRoleAuthority(role.getId(), role.getModifier(), authorityDTO)
                            .switchIfEmpty(Mono.empty()))
                    .collect(Collectors.toList());
            return monoList.stream().sequential().map(authorityMono -> authorityMono.map(authorityRepository::save));
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
     * 初始设置UserRole参数
     *
     * @param roleId           角色主键
     * @param modifier         修改人
     * @param roleAuthorityDTO 资源信息
     * @return 用户-角色对象
     */
    private Mono<Authority> initRoleAuthority(String roleId, String modifier, RoleAuthorityDTO roleAuthorityDTO) {
        Mono<Authority> authorityMono = authorityRepository.findByCodeAndEnabledTrue(roleAuthorityDTO.getAuthorityCode())
                .switchIfEmpty(Mono.error(NotContextException::new));

        return authorityMono.map(authority -> {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(roleId);
            roleAuthority.setHasWrite(roleAuthorityDTO.isHasWrite());
            roleAuthority.setModifier(modifier);
            return authority;
        });
    }

}
