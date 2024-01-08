/*
 *  Copyright 2018-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Privilege;
import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import io.leafage.basic.hypervisor.repository.RoleComponentsRepository;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * privilege service impl
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class PrivilegeServiceImpl extends ReactiveAbstractTreeNodeService<Privilege> implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final RoleMembersRepository roleMembersRepository;
    private final UserRepository userRepository;
    private final RoleComponentsRepository roleComponentsRepository;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository, RoleMembersRepository roleMembersRepository,
                                UserRepository userRepository, RoleComponentsRepository roleComponentsRepository) {
        this.privilegeRepository = privilegeRepository;
        this.roleMembersRepository = roleMembersRepository;
        this.userRepository = userRepository;
        this.roleComponentsRepository = roleComponentsRepository;
    }

    @Override
    public Mono<Page<PrivilegeVO>> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Flux<PrivilegeVO> voFlux = privilegeRepository.findByEnabledTrue(pageable).flatMap(this::convertOuter);

        Mono<Long> count = privilegeRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    @Override
    public Mono<List<TreeNode>> tree() {
        Flux<Privilege> privilegeFlux = privilegeRepository.findAll();
        return this.expandAndConvert(privilegeFlux);
    }

    @Override
    public Flux<PrivilegeVO> retrieve() {
        return privilegeRepository.findAll().flatMap(this::convertOuter);
    }

    @Override
    public Mono<List<TreeNode>> privileges(String username) {
        Assert.hasText(username, "username must not be blank.");
        Mono<User> accountMono = userRepository.getByUsername(username)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));

        return accountMono.map(user -> roleMembersRepository.findByUsername(user.getUsername())
                        .flatMap(userRole -> roleComponentsRepository.findByRoleId(userRole.getRoleId())
                                .flatMap(roleComponents -> privilegeRepository.findById(roleComponents.getPrivilegeId()))))
                .flatMap(this::expandAndConvert);
    }

    @Override
    public Mono<Boolean> exist(String privilegeName) {
        Assert.hasText(privilegeName, "privilege name must not be blank.");
        return privilegeRepository.existsByPrivilegeName(privilegeName);
    }

    @Override
    public Mono<PrivilegeVO> fetch(Long id) {
        Assert.notNull(id, "privilege id must not be null.");
        return privilegeRepository.findById(id).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<PrivilegeVO> create(PrivilegeDTO privilegeDTO) {
        return Mono.just(privilegeDTO).map(dto -> {
            Privilege privilege = new Privilege();
            BeanUtils.copyProperties(dto, privilege);
            return privilege;
        }).flatMap(privilegeRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<PrivilegeVO> modify(Long id, PrivilegeDTO privilegeDTO) {
        Assert.notNull(id, "privilege id must not be null.");
        return privilegeRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(privilege -> {
                    BeanUtils.copyProperties(privilegeDTO, privilege);
                    return privilegeRepository.save(privilege);
                }).flatMap(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param privilege 信息
     * @return 输出转换后的vo对象
     */
    private Mono<PrivilegeVO> convertOuter(Privilege privilege) {
        return Mono.just(privilege).map(a -> {
            PrivilegeVO privilegeVO = new PrivilegeVO();
            BeanUtils.copyProperties(privilege, privilegeVO);
            return privilegeVO;
        });
    }

    /**
     * convert to TreeNode
     *
     * @param privileges privilege集合
     * @return TreeNode of Flux
     */
    private Mono<List<TreeNode>> expandAndConvert(Flux<Privilege> privileges) {
        Set<String> expand = new HashSet<>();
        expand.add("icon");
        expand.add("path");
        return this.convert(privileges, expand);
    }

}