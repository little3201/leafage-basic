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

import io.leafage.basic.hypervisor.domain.GroupMembers;
import io.leafage.basic.hypervisor.repository.GroupMembersRepository;
import io.leafage.basic.hypervisor.service.GroupMembersService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * group members service impl.
 *
 * @author wq li 2021/11/27 14:18
 **/
@Service
public class GroupMembersServiceImpl implements GroupMembersService {

    private final GroupMembersRepository groupMembersRepository;

    public GroupMembersServiceImpl(GroupMembersRepository groupMembersRepository) {
        this.groupMembersRepository = groupMembersRepository;
    }

    @Override
    public List<GroupMembers> members(Long groupId) {
        Assert.notNull(groupId, "group id must not be null.");

        return groupMembersRepository.findByGroupId(groupId);
    }

    @Override
    public List<GroupMembers> groups(String username) {
        Assert.hasText(username, "username must not be blank.");

        return groupMembersRepository.findByUsername(username);
    }

    @Override
    public List<GroupMembers> relation(Long groupId, Set<String> usernames) {
        Assert.notNull(groupId, "group id must not be null.");
        Assert.notEmpty(usernames, "usernames must not be empty.");

        List<GroupMembers> groupMembers = usernames.stream().map(username -> {
            GroupMembers groupMember = new GroupMembers();
            groupMember.setGroupId(groupId);
            groupMember.setUsername(username);
            return groupMember;
        }).toList();
        return groupMembersRepository.saveAllAndFlush(groupMembers);
    }
}
