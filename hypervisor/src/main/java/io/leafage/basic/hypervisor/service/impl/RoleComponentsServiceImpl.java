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

import io.leafage.basic.hypervisor.domain.RoleComponents;
import io.leafage.basic.hypervisor.repository.ComponentRepository;
import io.leafage.basic.hypervisor.repository.RoleComponentsRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RoleComponentsService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * role authority service impl
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
@Service
public class RoleComponentsServiceImpl implements RoleComponentsService {

    private final RoleRepository roleRepository;
    private final RoleComponentsRepository roleComponentsRepository;
    private final ComponentRepository componentRepository;

    public RoleComponentsServiceImpl(RoleRepository roleRepository, RoleComponentsRepository roleComponentsRepository, ComponentRepository componentRepository) {
        this.roleRepository = roleRepository;
        this.roleComponentsRepository = roleComponentsRepository;
        this.componentRepository = componentRepository;
    }

    @Override
    public Mono<List<RoleComponents>> components(Long roleId) {
        Assert.notNull(roleId, "role id must not be null.");
        return roleComponentsRepository.findByRoleId(roleId).collectList();
    }

    @Override
    public Mono<List<RoleComponents>> roles(Long componentId) {
        Assert.notNull(componentId, "component id must not be null.");
        return roleComponentsRepository.findByComponentId(componentId).collectList();
    }

    @Override
    public Mono<Boolean> relation(Long roleId, Set<Long> componentIds) {
        Assert.notNull(roleId, "role id must not be null.");
        Assert.notEmpty(componentIds, "component ids must not be empty.");

        return Flux.fromIterable(componentIds).map(componentId -> {
            RoleComponents roleComponents = new RoleComponents();
            roleComponents.setRoleId(roleId);
            roleComponents.setComponentId(componentId);
            return roleComponents;
        }).collectList().flatMapMany(roleComponentsRepository::saveAll).hasElements();
    }
}
