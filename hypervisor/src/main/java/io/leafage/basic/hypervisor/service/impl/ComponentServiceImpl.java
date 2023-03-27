/*
 *  Copyright 2018-2023 the original author or authors.
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

import io.leafage.basic.hypervisor.domain.Component;
import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.ComponentDTO;
import io.leafage.basic.hypervisor.repository.ComponentRepository;
import io.leafage.basic.hypervisor.repository.RoleComponentsRepository;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.ComponentService;
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
 * component service impl
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class ComponentServiceImpl extends ReactiveAbstractTreeNodeService<Component> implements ComponentService {

    private final ComponentRepository componentRepository;
    private final RoleMembersRepository roleMembersRepository;
    private final UserRepository userRepository;
    private final RoleComponentsRepository roleComponentsRepository;

    public ComponentServiceImpl(ComponentRepository componentRepository, RoleMembersRepository roleMembersRepository,
                                UserRepository userRepository, RoleComponentsRepository roleComponentsRepository) {
        this.componentRepository = componentRepository;
        this.roleMembersRepository = roleMembersRepository;
        this.userRepository = userRepository;
        this.roleComponentsRepository = roleComponentsRepository;
    }

    @Override
    public Mono<Page<ComponentVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<ComponentVO> voFlux = componentRepository.findAll(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = componentRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<List<TreeNode>> tree() {
        Flux<Component> componentFlux = componentRepository.findAll();
        return this.expandAndConvert(componentFlux);
    }

    @Override
    public Flux<ComponentVO> retrieve() {
        return componentRepository.findAll().flatMap(this::convertOuter);
    }

    @Override
    public Mono<List<TreeNode>> components(String username) {
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
        Assert.hasText(componentName, "component name must not be blank.");
        return componentRepository.existsByComponentName(componentName);
    }

    @Override
    public Mono<ComponentVO> fetch(Long id) {
        Assert.notNull(id, "component id must not be null.");
        return componentRepository.findById(id).flatMap(this::convertOuter)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    public Mono<ComponentVO> create(ComponentDTO componentDTO) {
        return Mono.just(componentDTO).map(dto -> {
            Component component = new Component();
            BeanUtils.copyProperties(dto, component);
            return component;
        }).flatMap(componentRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<ComponentVO> modify(Long id, ComponentDTO componentDTO) {
        Assert.notNull(id, "component id must not be null.");
        return componentRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(component -> {
                    BeanUtils.copyProperties(componentDTO, component);
                    return componentRepository.save(component);
                }).flatMap(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param component 信息
     * @return 输出转换后的vo对象
     */
    private Mono<ComponentVO> convertOuter(Component component) {
        return Mono.just(component).map(a -> {
            ComponentVO componentVO = new ComponentVO();
            BeanUtils.copyProperties(component, componentVO);
            return componentVO;
        });
    }

    /**
     * convert to TreeNode
     *
     * @param components component集合
     * @return TreeNode of Flux
     */
    private Mono<List<TreeNode>> expandAndConvert(Flux<Component> components) {
        Set<String> expand = new HashSet<>();
        expand.add("icon");
        expand.add("path");
        return this.convert(components, expand);
    }

}
