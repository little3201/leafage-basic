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
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * group roles service impl.
 *
 * @author wq li 2024/2/2 15:20
 **/
public class GroupRolesServiceImpl implements GroupRolesService {

    private final GroupRolesRepository groupRolesRepository;

    public GroupRolesServiceImpl(GroupRolesRepository groupRolesRepository) {
        this.groupRolesRepository = groupRolesRepository;
    }

    @Override
    public List<GroupRoles> roles(Long groupId) {
        return groupRolesRepository.findByGroupId(groupId);
    }

    @Override
    public List<GroupRoles> groups(Long roleId) {
        return groupRolesRepository.findByRoleId(roleId);
    }

    @Override
    public List<GroupRoles> relation(Long groupId, Set<Long> roleIds) {
        Assert.notNull(groupId, "group id must not be null.");
        Assert.notEmpty(roleIds, "role ids must not be empty.");

        List<GroupRoles> groupRoles = roleIds.stream().map(roleId -> {
            GroupRoles groupRole = new GroupRoles();
            groupRole.setGroupId(groupId);
            groupRole.setRoleId(roleId);
            return groupRole;
        }).toList();
        return groupRolesRepository.saveAllAndFlush(groupRoles);
    }
}
