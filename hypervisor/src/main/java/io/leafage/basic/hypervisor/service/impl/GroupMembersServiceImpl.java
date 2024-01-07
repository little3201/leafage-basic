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

import io.leafage.basic.hypervisor.domain.GroupMembers;
import io.leafage.basic.hypervisor.repository.GroupMembersRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.GroupMembersService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * group members service impl
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
@Service
public class GroupMembersServiceImpl implements GroupMembersService {

    private final GroupRepository groupRepository;
    private final GroupMembersRepository groupMembersRepository;
    private final UserRepository userRepository;

    public GroupMembersServiceImpl(GroupRepository groupRepository, GroupMembersRepository groupMembersRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMembersRepository = groupMembersRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<List<GroupMembers>> members(Long groupId) {
        Assert.notNull(groupId, "group id must not be null.");
        return groupMembersRepository.findByGroupId(groupId).collectList();
    }

    @Override
    public Mono<List<GroupMembers>> groups(String username) {
        Assert.hasText(username, "username must not be blank.");
        return groupMembersRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(NoSuchElementException::new)).collectList();
    }

    @Override
    public Mono<Boolean> relation(String username, Set<Long> groupIds) {
        Assert.hasText(username, "username must not be blank.");
        Assert.notEmpty(groupIds, "group ids must not be empty.");

        return Flux.fromIterable(groupIds).map(groupId -> {
            GroupMembers groupMembers = new GroupMembers();
            groupMembers.setUsername(username);
            groupMembers.setGroupId(groupId);
            return groupMembers;
        }).collectList().flatMapMany(groupMembersRepository::saveAll).hasElements();
    }
}
