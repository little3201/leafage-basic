/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 角色信息service 接口实现
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends ReactiveAbstractTreeNodeService<Role> implements RoleService {

    private static final String MESSAGE = "code is blank.";

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public RoleServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Flux<RoleVO> retrieve() {
        return roleRepository.findByEnabledTrue().flatMap(this::convertOuter);
    }

    @Override
    public Flux<RoleVO> retrieve(int page, int size) {
        return roleRepository.findByEnabledTrue(PageRequest.of(page, size))
                .flatMap(this::convertOuter);
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Role> roleFlux = roleRepository.findByEnabledTrue().switchIfEmpty(Mono.error(NoSuchElementException::new));
        return roleFlux.filter(r -> Objects.isNull(r.getSuperior())).flatMap(role -> {
            TreeNode treeNode = new TreeNode(role.getCode(), role.getName());
            return this.children(role, roleFlux).collectList().map(treeNodes -> {
                treeNode.setChildren(treeNodes);
                return treeNode;
            });
        });
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, "name is blank.");
        return roleRepository.existsByName(name);
    }

    @Override
    public Mono<RoleVO> fetch(String code) {
        Assert.hasText(code, MESSAGE);
        return roleRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<Long> count() {
        return roleRepository.count();
    }

    @Override
    public Mono<RoleVO> create(RoleDTO roleDTO) {
        Mono<Role> roleMono = Mono.just(roleDTO).map(dto -> {
            Role role = new Role();
            BeanUtils.copyProperties(roleDTO, role);
            role.setCode(this.generateCode());
            return role;
        });

        return this.superior(roleDTO.getSuperior(), roleMono).flatMap(role -> {
            BeanUtils.copyProperties(roleDTO, role);
            return roleRepository.insert(role);
        }).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RoleVO> modify(String code, RoleDTO roleDTO) {
        Assert.hasText(code, MESSAGE);
        Mono<Role> roleMono = roleRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));

        return this.superior(roleDTO.getSuperior(), roleMono).flatMap(role -> {
            BeanUtils.copyProperties(roleDTO, role);
            return roleRepository.save(role);
        }).flatMap(this::convertOuter);
    }


    /**
     * 设置上级
     *
     * @param superior 上级code
     * @param roleMono 当前对象
     * @return 设置上级后的对象
     */
    private Mono<Role> superior(String superior, Mono<Role> roleMono) {
        // 如果设置上级，进行处理
        if (StringUtils.hasText(superior)) {
            Mono<Role> superiorMono = roleRepository.getByCodeAndEnabledTrue(superior)
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));

            return roleMono.zipWith(superiorMono, (authority, sup) -> {
                authority.setSuperior(sup.getId());
                return authority;
            });
        }
        return roleMono;
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param role 信息
     * @return 输出转换后的vo对象
     */
    private Mono<RoleVO> fetchOuter(Role role) {
        Mono<RoleVO> voMono = Mono.just(role).map(a -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        });

        if (role.getSuperior() != null) {
            Mono<Role> superiorMono = roleRepository.findById(role.getSuperior());
            voMono = voMono.zipWith(superiorMono, (a, superior) -> {
                a.setSuperior(superior.getCode());
                return a;
            });
        }
        return voMono;
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param role 信息
     * @return 输出转换后的vo对象
     */
    private Mono<RoleVO> convertOuter(Role role) {
        Mono<RoleVO> voMono = Mono.just(role).map(r -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(r, roleVO);
            return roleVO;
        });

        Mono<Long> longMono = userRoleRepository.countByRoleIdAndEnabledTrue(role.getId())
                .switchIfEmpty(Mono.just(0L));
        voMono = voMono.zipWith(longMono, (vo, count) -> {
            vo.setCount(count);
            return vo;
        });

        if (role.getSuperior() != null) {
            Mono<Role> superiorMono = roleRepository.findById(role.getSuperior());
            voMono = voMono.zipWith(superiorMono, (vo, superior) -> {
                vo.setSuperior(superior.getName());
                return vo;
            });
        }
        return voMono;
    }
}
