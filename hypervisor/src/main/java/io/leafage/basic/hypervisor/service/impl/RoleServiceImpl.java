/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.domain.TreeNode;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 角色信息service 接口实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends AbstractBasicService implements RoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    public RoleServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository,
                           AuthorityRepository authorityRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Flux<RoleVO> retrieve() {
        return roleRepository.findAll().map(this::convertOuter);
    }

    @Override
    public Flux<RoleVO> retrieve(int page, int size) {
        return roleRepository.findByEnabledTrue(PageRequest.of(page, size))
                .flatMap(role -> userRoleRepository.countByRoleIdAndEnabledTrue(role.getId())
                        .flatMap(count -> {
                            RoleVO roleVO = this.convertOuter(role);
                            roleVO.setCount(count);
                            if (role.getSuperior() != null) {
                                return roleRepository.findById(role.getSuperior()).map(superior -> {
                                    roleVO.setSuperior(superior.getName());
                                    return roleVO;
                                });
                            }
                            return Mono.just(roleVO);
                        })
                );
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Role> roleFlux = roleRepository.findByEnabledTrue();
        return roleFlux.filter(r -> Objects.isNull(r.getSuperior())).flatMap(role -> {
            TreeNode treeNode = new TreeNode(role.getCode(), role.getName());
            return this.addChildren(role, roleFlux).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }

    @Override
    public Mono<RoleVO> fetch(String code) {
        Assert.hasText(code, "code is blank");
        return roleRepository.getByCodeAndEnabledTrue(code).flatMap(role -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            if (role.getSuperior() != null) {
                return roleRepository.findById(role.getSuperior()).map(superior -> {
                    roleVO.setSuperior(superior.getCode());
                    return roleVO;
                });
            }
            return Mono.just(roleVO);
        });
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
        // 如果设置上级，进行处理
        Mono<Role> roleMono;
        if (StringUtils.hasText(roleDTO.getSuperior())) {
            roleMono = roleRepository.getByCodeAndEnabledTrue(roleDTO.getSuperior())
                    .switchIfEmpty(Mono.error(NotContextException::new)).map(superior -> {
                        role.setSuperior(superior.getId());
                        return role;
                    });
        } else {
            roleMono = Mono.just(role);
        }
        return roleMono.flatMap(roleRepository::insert).map(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(String code, RoleDTO roleDTO) {
        Assert.hasText(code, "code is blank");
        return roleRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(role -> {
                    BeanUtils.copyProperties(roleDTO, role);
                    if (StringUtils.hasText(roleDTO.getSuperior())) {
                        return roleRepository.getByCodeAndEnabledTrue(roleDTO.getSuperior()).map(superior -> {
                            role.setSuperior(superior.getId());
                            return role;
                        });
                    }
                    return roleRepository.save(role);
                }).map(this::convertOuter);
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

    /**
     * add child node
     *
     * @param superior superior node
     * @param roleFlux to be build source data
     * @return tree node
     */
    private Flux<TreeNode> addChildren(Role superior, Flux<Role> roleFlux) {
        return roleFlux.filter(role -> superior.getId().equals(role.getSuperior())).flatMap(role -> {
            TreeNode treeNode = new TreeNode(role.getCode(), role.getName());
            treeNode.setSuperior(superior.getCode());
            return this.addChildren(role, roleFlux).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }
}
