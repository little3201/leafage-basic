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

import io.leafage.basic.hypervisor.domain.Privilege;
import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import io.leafage.basic.hypervisor.repository.RoleComponentsRepository;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
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
 * privilege service test
 *
 * @author liwenqiang 2019-01-29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class PrivilegeServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleMembersRepository roleMembersRepository;

    @Mock
    private PrivilegeRepository privilegeRepository;

    @Mock
    private RoleComponentsRepository roleComponentsRepository;

    @InjectMocks
    private PrivilegeServiceImpl authorityService;

    private PrivilegeDTO componentDTO;
    private User user;

    @BeforeEach
    void init() {
        componentDTO = new PrivilegeDTO();
        componentDTO.setPrivilegeName("test");
        componentDTO.setType('M');

        user = new User();
        user.setUsername("test");
    }

    @Test
    void retrieve_page() {
        given(this.privilegeRepository.findByEnabledTrue(Mockito.any(PageRequest.class))).willReturn(Flux.just(Mockito.mock(Privilege.class)));

        given(this.privilegeRepository.count()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(authorityService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve() {
        given(this.privilegeRepository.findAll()).willReturn(Flux.just(Mockito.mock(Privilege.class)));

        StepVerifier.create(authorityService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.privilegeRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Privilege.class)));

        StepVerifier.create(authorityService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch_no_superior() {
        given(this.privilegeRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Privilege.class)));

        StepVerifier.create(authorityService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.privilegeRepository.save(Mockito.any(Privilege.class))).willReturn(Mono.just(Mockito.mock(Privilege.class)));

        StepVerifier.create(authorityService.create(componentDTO)).expectNextCount(1).verifyComplete();
    }


    @Test
    void create_no_superior() {
        given(this.privilegeRepository.save(Mockito.any(Privilege.class))).willReturn(Mono.just(Mockito.mock(Privilege.class)));

        StepVerifier.create(authorityService.create(componentDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.privilegeRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Privilege.class)));

        given(this.privilegeRepository.save(Mockito.any(Privilege.class))).willReturn(Mono.just(Mockito.mock(Privilege.class)));

        StepVerifier.create(authorityService.modify(1L, componentDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void tree() {
        Privilege privilege = new Privilege();
        privilege.setId(1L);
        privilege.setPrivilegeName("21612OL34");
        privilege.setIcon("test");

        Privilege child = new Privilege();
        child.setId(2L);
        child.setSuperiorId(privilege.getId());
        child.setPrivilegeName("21612OL35");
        child.setType('M');
        given(this.privilegeRepository.findAll()).willReturn(Flux.just(privilege, child));

        StepVerifier.create(authorityService.tree()).expectNextCount(1).verifyComplete();
    }

    @Test
    void privileges() {
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(user));

        given(this.roleMembersRepository.findByUsername(Mockito.anyString())).willReturn(Flux.just(Mockito.mock(RoleMembers.class)));

        given(this.roleComponentsRepository.findByRoleId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(RolePrivileges.class)));

        given(this.privilegeRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Privilege.class)));

        StepVerifier.create(authorityService.privileges("little3201")).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.privilegeRepository.existsByPrivilegeName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(authorityService.exist("little3201")).expectNext(Boolean.TRUE).verifyComplete();
    }
}