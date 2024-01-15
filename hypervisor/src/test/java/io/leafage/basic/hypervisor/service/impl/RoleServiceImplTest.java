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

import io.leafage.basic.hypervisor.domain.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.RoleRepository;
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
 * role接口测试
 *
 * @author liwenqiang 2019-01-29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleDTO roleDTO;

    @BeforeEach
    void init() {
        roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setSuperiorId(1L);
    }

    @Test
    void retrieve() {
        given(this.roleRepository.findAll()).willReturn(Flux.just(Mockito.mock(Role.class)));

        StepVerifier.create(roleService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        given(this.roleRepository.findBy(Mockito.any(PageRequest.class))).willReturn(Flux.just(Mockito.mock(Role.class)));

        given(this.roleRepository.count()).willReturn(Mono.just(2L));

        StepVerifier.create(roleService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Role.class)));

        StepVerifier.create(roleService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.roleRepository.save(Mockito.any(Role.class))).willReturn(Mono.just(Mockito.mock(Role.class)));

        StepVerifier.create(roleService.create(roleDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create_error() {
        given(this.roleRepository.save(Mockito.any(Role.class))).willThrow(new RuntimeException());

        StepVerifier.create(roleService.create(roleDTO)).expectError(RuntimeException.class).verify();
    }

    @Test
    void modify() {
        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Role.class)));

        given(this.roleRepository.save(Mockito.any(Role.class))).willReturn(Mono.just(Mockito.mock(Role.class)));

        StepVerifier.create(roleService.modify(1L, roleDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.roleRepository.existsByRoleName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(roleService.exist("vip")).expectNext(Boolean.TRUE).verifyComplete();
    }
}