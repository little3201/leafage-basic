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
import io.leafage.basic.hypervisor.document.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.AccountRoleRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
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

import static org.mockito.BDDMockito.given;

/**
 * role接口测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AccountRoleRepository accountRoleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void retrieve() {
        Role role = new Role();
        role.setId(new ObjectId());
        role.setSuperior(new ObjectId());
        given(this.roleRepository.findByEnabledTrue()).willReturn(Flux.just(role));

        given(this.accountRoleRepository.countByRoleIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(role));
        StepVerifier.create(roleService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        Role role = new Role();
        role.setId(new ObjectId());
        role.setSuperior(new ObjectId());
        given(this.roleRepository.findByEnabledTrue(Mockito.any(Pageable.class))).willReturn(Flux.just(role));

        given(this.accountRoleRepository.countByRoleIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        role.setName("test");
        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(role));

        given(this.roleRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(roleService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Role role = new Role();
        role.setId(new ObjectId());
        role.setName("test");
        role.setSuperior(new ObjectId());
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(role));

        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Role.class)));

        StepVerifier.create(roleService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch_no_superior() {
        Role role = new Role();
        role.setId(new ObjectId());
        role.setName("test");
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(role));

        StepVerifier.create(roleService.fetch("21612OL34")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Role.class)));

        Role role = new Role();
        role.setId(new ObjectId());
        given(this.roleRepository.insert(Mockito.any(Role.class))).willReturn(Mono.just(role));

        given(this.accountRoleRepository.countByRoleIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setAlias("Test");
        roleDTO.setDescription("描述信息");

        SimpleBO<String> simpleBO = new SimpleBO<>();
        simpleBO.setCode("21612OL12");
        simpleBO.setName("Test");
        roleDTO.setSuperior(simpleBO);
        StepVerifier.create(roleService.create(roleDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create_no_superior() {
        Role role = new Role();
        role.setId(new ObjectId());
        given(this.roleRepository.insert(Mockito.any(Role.class))).willReturn(Mono.just(role));

        given(this.accountRoleRepository.countByRoleIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setAlias("Test");
        roleDTO.setDescription("描述信息");

        StepVerifier.create(roleService.create(roleDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.roleRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Role.class)));

        Role role = new Role();
        role.setId(new ObjectId());
        role.setName("test");
        role.setSuperior(new ObjectId());
        given(this.roleRepository.save(Mockito.any(Role.class))).willReturn(Mono.just(role));

        given(this.accountRoleRepository.countByRoleIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(role));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setAlias("Test");
        roleDTO.setDescription("描述信息");

        SimpleBO<String> simpleBO = new SimpleBO<>();
        simpleBO.setCode("21612OL12");
        simpleBO.setName("Test");
        roleDTO.setSuperior(simpleBO);
        StepVerifier.create(roleService.modify("21612OL34", roleDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void tree() {
        Role role = new Role();
        ObjectId id = new ObjectId();
        role.setId(id);
        role.setCode("21612OL35");
        role.setName("test");
        role.setModifier(new ObjectId());

        Role child = new Role();
        child.setId(new ObjectId());
        child.setSuperior(id);
        child.setCode("21612OL34");
        child.setName("test-sub");
        child.setModifier(role.getModifier());
        child.setEnabled(role.isEnabled());
        given(this.roleRepository.findByEnabledTrue()).willReturn(Flux.just(role, child));

        given(this.roleRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Role.class)));

        StepVerifier.create(roleService.tree()).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.roleRepository.existsByName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(roleService.exist("vip")).expectNext(Boolean.TRUE).verifyComplete();
    }
}