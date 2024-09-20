/*
 *  Copyright 2018-2024 little3201.
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

import io.leafage.basic.hypervisor.domain.GroupRoles;
import io.leafage.basic.hypervisor.repository.GroupRolesRepository;
import io.leafage.basic.hypervisor.service.GroupRolesService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * group roles service impl
 *
 * @author wq li
 */
@Service
public class GroupRolesServiceImpl implements GroupRolesService {

    private final GroupRolesRepository groupRolesRepository;

    /**
     * <p>Constructor for GroupRolesServiceImpl.</p>
     *
     * @param groupRolesRepository a {@link io.leafage.basic.hypervisor.repository.GroupRolesRepository} object
     */
    public GroupRolesServiceImpl(GroupRolesRepository groupRolesRepository) {
        this.groupRolesRepository = groupRolesRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<List<GroupRoles>> roles(Long groupId) {
        Assert.notNull(groupId, "group id must not be null.");
        return groupRolesRepository.findByGroupId(groupId).collectList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<List<GroupRoles>> groups(Long roleId) {
        Assert.notNull(roleId, "role id must not be blank.");
        return groupRolesRepository.findByRoleId(roleId).collectList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> relation(Long groupId, Set<Long> roleIds) {
        Assert.notNull(groupId, "group id must not be blank.");
        Assert.notEmpty(roleIds, "role ids must not be empty.");

        return Flux.fromIterable(roleIds).map(roleId -> {
            GroupRoles groupRoles = new GroupRoles();
            groupRoles.setRoleId(roleId);
            groupRoles.setGroupId(groupId);
            return groupRoles;
        }).flatMap(groupRolesRepository::save).all(groupRoles -> Boolean.TRUE);
    }
}
