/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.BasicVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import top.leafage.common.basic.ValidMessage;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.NoSuchElementException;

/**
 * role service impl
 *
 * @author liwenqiang 2018/9/27 14:20
 **/
@Service
public class RoleServiceImpl extends ReactiveAbstractTreeNodeService<Role> implements RoleService {

    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;

    public RoleServiceImpl(AccountRoleRepository accountRoleRepository, RoleRepository roleRepository) {
        this.accountRoleRepository = accountRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Flux<RoleVO> retrieve() {
        return roleRepository.findByEnabledTrue().flatMap(this::convertOuterWithCount);
    }

    @Override
    public Mono<Page<RoleVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<RoleVO> voFlux = roleRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuterWithCount);

        Mono<Long> count = roleRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Flux<TreeNode> tree() {
        Flux<Role> roleFlux = roleRepository.findByEnabledTrue()
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
        return this.convert(roleFlux);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return roleRepository.existsByName(name);
    }

    @Override
    public Mono<RoleVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return roleRepository.getByCodeAndEnabledTrue(code).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<RoleVO> create(RoleDTO roleDTO) {
        return Mono.just(roleDTO).map(dto -> {
                    Role role = new Role();
                    BeanUtils.copyProperties(roleDTO, role);
                    role.setCode(this.generateCode());
                    return role;
                }).flatMap(vo -> this.superior(roleDTO.getSuperior(), vo))
                .switchIfEmpty(Mono.error(NoSuchElementException::new)).flatMap(role -> {
                    BeanUtils.copyProperties(roleDTO, role);
                    return roleRepository.insert(role);
                }).flatMap(this::convertOuterWithCount);
    }

    @Override
    public Mono<RoleVO> modify(String code, RoleDTO roleDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return roleRepository.getByCodeAndEnabledTrue(code)
                .flatMap(vo -> this.superior(roleDTO.getSuperior(), vo))
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(role -> {
                    BeanUtils.copyProperties(roleDTO, role);
                    return roleRepository.save(role);
                }).flatMap(this::convertOuterWithCount);
    }

    /**
     * 设置上级
     *
     * @param superior 上级code
     * @param role     当前对象
     * @return 设置上级后的对象
     */
    private Mono<Role> superior(String superior, Role role) {
        return Mono.just(role).flatMap(r -> {
            if (!StringUtils.hasText(superior)) {
                return Mono.just(r);
            }
            return roleRepository.getByCodeAndEnabledTrue(superior).map(s -> {
                        r.setSuperior(s.getId());
                        return r;
                    })
                    .switchIfEmpty(Mono.error(NoSuchElementException::new));
        });
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param role 信息
     * @return 输出转换后的vo对象
     */
    private Mono<RoleVO> convertOuter(Role role) {
        return Mono.just(role).map(a -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        }).flatMap(vo -> superior(role.getSuperior(), vo));
    }

    /**
     * 转换superior
     *
     * @param superiorId superior主键
     * @param vo         vo
     * @return 设置后的vo
     */
    private Mono<RoleVO> superior(ObjectId superiorId, RoleVO vo) {
        if (superiorId != null) {
            return roleRepository.findById(superiorId).map(superior -> {
                BasicVO<String> basicVO = new BasicVO<>();
                BeanUtils.copyProperties(superior, basicVO);
                vo.setSuperior(basicVO);
                return vo;
            }).switchIfEmpty(Mono.just(vo));
        }
        return Mono.just(vo);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param role 信息
     * @return 输出转换后的vo对象
     */
    private Mono<RoleVO> convertOuterWithCount(Role role) {
        return this.convertOuter(role).flatMap(vo ->
                accountRoleRepository.countByRoleIdAndEnabledTrue(role.getId()).map(count -> {
                    vo.setCount(count);
                    return vo;
                }));
    }
}
