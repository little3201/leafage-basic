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

import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.service.RoleMembersService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * role members service impl.
 *
 * @author wq li 2021/11/27 14:18
 **/
@Service
public class RoleMembersServiceImpl implements RoleMembersService {

    private final RoleMembersRepository roleMembersRepository;

    public RoleMembersServiceImpl(RoleMembersRepository roleMembersRepository) {
        this.roleMembersRepository = roleMembersRepository;
    }

    @Override
    public List<RoleMembers> members(Long roleId) {
        Assert.notNull(roleId, "role id must not be null.");

        return roleMembersRepository.findByRoleId(roleId);
    }

    @Override
    public List<RoleMembers> roles(String username) {
        Assert.hasText(username, "username must not be blank.");

        return roleMembersRepository.findByUsername(username);
    }

    @Override
    public List<RoleMembers> relation(Long roleId, Set<String> usernames) {
        Assert.notNull(roleId, "role id must not be null.");
        Assert.notEmpty(usernames, "usernames must not be empty.");

        List<RoleMembers> roleMembers = usernames.stream().map(username -> {
            RoleMembers roleMember = new RoleMembers();
            roleMember.setRoleId(roleId);
            roleMember.setUsername(username);
            return roleMember;
        }).toList();
        return roleMembersRepository.saveAllAndFlush(roleMembers);
    }
}
