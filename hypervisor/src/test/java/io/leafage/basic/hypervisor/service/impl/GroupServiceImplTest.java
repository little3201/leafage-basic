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

import io.leafage.basic.hypervisor.bo.SimpleBO;
import io.leafage.basic.hypervisor.document.Account;
import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.repository.AccountGroupRepository;
import io.leafage.basic.hypervisor.repository.AccountRepository;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

/**
 * group接口测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private AccountGroupRepository accountGroupRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    @Test
    void retrieve() {
        Group group = new Group();
        group.setId(new ObjectId());
        group.setAlias("技术");
        group.setSuperior(new ObjectId());
        group.setPrincipal(new ObjectId());
        given(this.groupRepository.findByEnabledTrue()).willReturn(Flux.just(group));

        given(this.accountGroupRepository.countByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Group.class)));

        given(this.accountRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Account.class)));

        StepVerifier.create(groupService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        Group group = new Group();
        group.setId(new ObjectId());
        group.setSuperior(new ObjectId());
        group.setName("test");
        group.setPrincipal(new ObjectId());
        given(this.groupRepository.findByEnabledTrue(Mockito.any(Pageable.class))).willReturn(Flux.just(group));

        given(this.accountGroupRepository.countByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Group.class)));

        given(this.accountRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Account.class)));

        given(this.groupRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(groupService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Group group = new Group();
        group.setId(new ObjectId());
        group.setSuperior(new ObjectId());
        group.setPrincipal(new ObjectId());
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(group));

        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Group.class)));

        given(this.accountRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Account.class)));

        StepVerifier.create(groupService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch_no_superior_principal() {
        Group group = new Group();
        group.setId(new ObjectId());
        group.setName("test");
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(group));

        StepVerifier.create(groupService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Group.class)));

        Group group = new Group();
        group.setId(new ObjectId());
        given(this.groupRepository.insert(Mockito.any(Group.class))).willReturn(Mono.just(group));

        given(this.accountGroupRepository.countByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setAlias("Test");

        SimpleBO<String> simpleBO = new SimpleBO<>();
        simpleBO.setCode("21612OL34");
        simpleBO.setName("Test");
        groupDTO.setSuperior(simpleBO);
        StepVerifier.create(groupService.create(groupDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create_no_superior() {
        Group group = new Group();
        group.setId(new ObjectId());
        given(this.groupRepository.insert(Mockito.any(Group.class))).willReturn(Mono.just(group));

        given(this.accountGroupRepository.countByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setAlias("Test");
        StepVerifier.create(groupService.create(groupDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Group.class)));

        Group group = new Group();
        group.setId(new ObjectId());
        group.setSuperior(new ObjectId());
        given(this.groupRepository.save(Mockito.any(Group.class))).willReturn(Mono.just(group));

        given(this.accountGroupRepository.countByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Group.class)));

        given(this.accountRepository.getByUsernameAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Account.class)));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setAlias("Test");
        groupDTO.setPrincipal("little3201");

        SimpleBO<String> simpleBO = new SimpleBO<>();
        simpleBO.setCode("21612OL34");
        simpleBO.setName("Test");
        groupDTO.setSuperior(simpleBO);
        StepVerifier.create(groupService.modify("21612OL34", groupDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Group group = new Group();
        group.setId(new ObjectId());
        group.setEnabled(false);
        group.setModifier(new ObjectId());
        group.setModifyTime(LocalDateTime.now());
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(group));

        given(this.groupRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(groupService.remove("21612OL34")).verifyComplete();
    }

    @Test
    void tree() {
        Group group = new Group();
        ObjectId id = new ObjectId();
        group.setId(id);
        group.setCode("21612OL35");
        group.setName("test");

        Group child = new Group();
        child.setId(new ObjectId());
        child.setSuperior(id);
        child.setCode("21612OL34");
        child.setName("test-sub");
        given(this.groupRepository.findByEnabledTrue()).willReturn(Flux.just(group, child));

        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Group.class)));

        StepVerifier.create(groupService.tree()).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.groupRepository.existsByName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(groupService.exist("vip")).expectNext(Boolean.TRUE).verifyComplete();
    }
}