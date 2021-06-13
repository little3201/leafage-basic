/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Group;
import io.leafage.basic.hypervisor.document.User;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserGroupRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import org.bson.types.ObjectId;
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
 * 组service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    @Test
    void retrieve() {
        given(this.groupRepository.findByEnabledTrue()).willReturn(Flux.just(Mockito.mock(Group.class)));
        StepVerifier.create(groupService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        Group group = new Group();
        group.setId(new ObjectId());
        group.setSuperior(new ObjectId());
        group.setName("test");
        group.setPrincipal(new ObjectId());
        given(this.groupRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(Flux.just(group));
        given(this.userGroupRepository.countByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));
        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(group));
        User user = new User();
        user.setNickname("test");
        StepVerifier.create(groupService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Group group = new Group();
        group.setId(new ObjectId());
        group.setSuperior(new ObjectId());
        given(this.groupRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(group));
        given(this.userGroupRepository.countByGroupIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));
        given(this.groupRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Group.class)));
        StepVerifier.create(groupService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
    }

    @Test
    void modify() {
    }

    @Test
    void remove() {
    }

    @Test
    void tree() {
        Group group = new Group();
        ObjectId id = new ObjectId();
        group.setId(id);
        Group child = new Group();
        child.setId(new ObjectId());
        child.setSuperior(id);
        given(this.groupRepository.findByEnabledTrue()).willReturn(Flux.just(group, child));
        StepVerifier.create(groupService.tree()).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.groupRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(groupService.count()).expectNextCount(1).verifyComplete();
    }
}