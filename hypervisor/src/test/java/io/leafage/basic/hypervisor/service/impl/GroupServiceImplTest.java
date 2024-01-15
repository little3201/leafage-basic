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

import io.leafage.basic.hypervisor.domain.Group;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * group service test
 *
 * @author liwenqiang 2019-01-29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    private GroupDTO groupDTO;

    @BeforeEach
    void init() {
        groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setPrincipal("Test");
    }

    @Test
    void retrieve() {
        given(this.groupRepository.findAll()).willReturn(Flux.just(Mockito.mock(Group.class)));

        StepVerifier.create(groupService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        given(this.groupRepository.findBy(Mockito.any(PageRequest.class))).willReturn(Flux.just(Mockito.mock(Group.class)));

        given(this.groupRepository.count()).willReturn(Mono.just(2L));

        StepVerifier.create(groupService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.groupRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Group.class)));

        StepVerifier.create(groupService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.groupRepository.save(Mockito.any(Group.class))).willReturn(Mono.just(Mockito.mock(Group.class)));

        StepVerifier.create(groupService.create(groupDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create_error() {
        given(this.groupRepository.save(Mockito.any(Group.class))).willThrow(new RuntimeException());

        StepVerifier.create(groupService.create(groupDTO)).expectError(RuntimeException.class).verify();
    }

    @Test
    void modify() {
        given(this.groupRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Group.class)));

        given(this.groupRepository.save(Mockito.any(Group.class))).willReturn(Mono.just(Mockito.mock(Group.class)));

        StepVerifier.create(groupService.modify(1L, groupDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        given(this.groupRepository.deleteById(Mockito.anyLong())).willReturn(Mono.empty());

        StepVerifier.create(groupService.remove(1L)).verifyComplete();
    }

    @Test
    void exist() {
        given(this.groupRepository.existsByGroupName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(groupService.exist("vip")).expectNext(Boolean.TRUE).verifyComplete();
    }
}