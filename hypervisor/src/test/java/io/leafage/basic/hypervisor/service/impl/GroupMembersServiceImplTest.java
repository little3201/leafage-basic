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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.BDDMockito.given;

/**
 * group members service test
 *
 * @author liwenqiang 2021-06-14 11:10
 **/
@ExtendWith(MockitoExtension.class)
class GroupMembersServiceImplTest {

    @Mock
    private GroupMembersRepository groupMembersRepository;

    @InjectMocks
    private GroupMembersServiceImpl userGroupService;

    @Test
    void members() {
        given(this.groupMembersRepository.findByGroupId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(GroupMembers.class)));

        StepVerifier.create(userGroupService.members(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void groups() {
        given(this.groupMembersRepository.findByUsername(Mockito.anyString())).willReturn(Flux.just(Mockito.mock(GroupMembers.class)));

        StepVerifier.create(userGroupService.groups("test")).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        given(this.groupMembersRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(GroupMembers.class)));

        StepVerifier.create(userGroupService.relation("test", Collections.singleton(1L)))
                .expectNextCount(1).verifyComplete();
    }
}