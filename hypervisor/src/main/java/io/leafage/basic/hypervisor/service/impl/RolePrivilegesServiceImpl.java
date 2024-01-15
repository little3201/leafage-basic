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

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * role privilege service impl
 *
 * @author liwenqiang 2018-07-28 0:29
 **/
@Service
public class RolePrivilegesServiceImpl implements RolePrivilegesService {

    private final RoleRepository roleRepository;
    private final RolePrivilegesRepository rolePrivilegesRepository;
    private final PrivilegeRepository privilegeRepository;

    public RolePrivilegesServiceImpl(RoleRepository roleRepository, RolePrivilegesRepository rolePrivilegesRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.rolePrivilegesRepository = rolePrivilegesRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public Mono<List<RolePrivileges>> privileges(Long roleId) {
        Assert.notNull(roleId, "role id must not be null.");
        return rolePrivilegesRepository.findByRoleId(roleId).collectList();
    }

    @Override
    public Mono<List<RolePrivileges>> roles(Long privilegeId) {
        Assert.notNull(privilegeId, "privilege id must not be null.");
        return rolePrivilegesRepository.findByComponentId(privilegeId).collectList();
    }

    @Override
    public Mono<Boolean> relation(Long roleId, Set<Long> privilegeIds) {
        Assert.notNull(roleId, "role id must not be null.");
        Assert.notEmpty(privilegeIds, "privilege ids must not be empty.");

        return Flux.fromIterable(privilegeIds).map(privilegeId -> {
            RolePrivileges rolePrivileges = new RolePrivileges();
            rolePrivileges.setRoleId(roleId);
            rolePrivileges.setPrivilegeId(privilegeId);
            return rolePrivileges;
        }).collectList().flatMapMany(rolePrivilegesRepository::saveAll).hasElements();
    }
}
