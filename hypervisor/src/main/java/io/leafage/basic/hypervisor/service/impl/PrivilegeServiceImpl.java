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
import io.leafage.basic.hypervisor.dto.ComponentDTO;
import io.leafage.basic.hypervisor.repository.ComponentRepository;
import io.leafage.basic.hypervisor.repository.RoleComponentsRepository;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.vo.ComponentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    private final ComponentRepository componentRepository;
    private final RoleMembersRepository roleMembersRepository;
    private final UserRepository userRepository;
    private final RoleComponentsRepository roleComponentsRepository;

    public PrivilegeServiceImpl(ComponentRepository componentRepository, RoleMembersRepository roleMembersRepository,
                                UserRepository userRepository, RoleComponentsRepository roleComponentsRepository) {
        this.componentRepository = componentRepository;
        this.roleMembersRepository = roleMembersRepository;
        this.userRepository = userRepository;
        this.roleComponentsRepository = roleComponentsRepository;
    }

    @Override
    public Mono<Page<ComponentVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<ComponentVO> voFlux = componentRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = componentRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<List<TreeNode>> tree() {
        Flux<Privilege> componentFlux = componentRepository.findAll();
        return this.expandAndConvert(componentFlux);
    }

    @Override
    public Flux<ComponentVO> retrieve() {
        return componentRepository.findAll().flatMap(this::convertOuter);
    }

    @Override
    public Mono<List<TreeNode>> privileges(String username) {
        Assert.hasText(username, "username must not be blank.");
        Mono<User> accountMono = userRepository.getByUsername(username)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));

        return accountMono.map(user -> roleMembersRepository.findByUsername(user.getUsername())
                        .flatMap(userRole -> roleComponentsRepository.findByRoleId(userRole.getRoleId())
                                .flatMap(roleComponents -> componentRepository.findById(roleComponents.getComponentId()))))
                .flatMap(this::expandAndConvert);
    }

    @Override
    public Mono<Boolean> exist(String componentName) {
        Assert.hasText(componentName, "privilege name must not be blank.");
        return componentRepository.existsByComponentName(componentName);
    }

    @Override
    public Mono<ComponentVO> fetch(Long id) {
        Assert.notNull(id, "privilege id must not be null.");
        return componentRepository.findById(id).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<ComponentVO> create(ComponentDTO componentDTO) {
        return Mono.just(componentDTO).map(dto -> {
            Privilege privilege = new Privilege();
            BeanUtils.copyProperties(dto, privilege);
            return privilege;
        }).flatMap(componentRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<ComponentVO> modify(Long id, ComponentDTO componentDTO) {
        Assert.notNull(id, "privilege id must not be null.");
        return componentRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(privilege -> {
                    BeanUtils.copyProperties(componentDTO, privilege);
                    return componentRepository.save(privilege);
                }).flatMap(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param privilege 信息
     * @return 输出转换后的vo对象
     */
    private Mono<ComponentVO> convertOuter(Privilege privilege) {
        return Mono.just(privilege).map(a -> {
            ComponentVO componentVO = new ComponentVO();
            BeanUtils.copyProperties(privilege, componentVO);
            return componentVO;
        });
    }

    /**
     * convert to TreeNode
     *
     * @param privileges component集合
     * @return TreeNode of Flux
     */
    private Mono<List<TreeNode>> expandAndConvert(Flux<Privilege> privileges) {
        Set<String> expand = new HashSet<>();
        expand.add("icon");
        expand.add("path");
        return this.convert(privileges, expand);
    }

}
