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

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * role privileges service impl
 *
 * @author wq li
 */
@Service
public class RolePrivilegesServiceImpl implements RolePrivilegesService {

    private final RolePrivilegesRepository rolePrivilegesRepository;

    /**
     * <p>Constructor for RolePrivilegesServiceImpl.</p>
     *
     * @param rolePrivilegesRepository a {@link io.leafage.basic.hypervisor.repository.RolePrivilegesRepository} object
     */
    public RolePrivilegesServiceImpl(RolePrivilegesRepository rolePrivilegesRepository) {
        this.rolePrivilegesRepository = rolePrivilegesRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<List<RolePrivileges>> privileges(Long roleId) {
        Assert.notNull(roleId, "role id must not be null.");
        return rolePrivilegesRepository.findByRoleId(roleId).collectList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<List<RolePrivileges>> roles(Long privilegeId) {
        Assert.notNull(privilegeId, "privilege id must not be null.");
        return rolePrivilegesRepository.findByPrivilegeId(privilegeId).collectList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> relation(Long roleId, Set<Long> privilegeIds) {
        Assert.notNull(roleId, "role id must not be null.");
        Assert.notEmpty(privilegeIds, "privilege ids must not be empty.");

        return Flux.fromIterable(privilegeIds).map(privilegeId -> {
            RolePrivileges rolePrivileges = new RolePrivileges();
            rolePrivileges.setRoleId(roleId);
            rolePrivileges.setPrivilegeId(privilegeId);
            return rolePrivileges;
        }).flatMap(rolePrivilegesRepository::save).all(rolePrivileges -> Boolean.TRUE);
    }
}
