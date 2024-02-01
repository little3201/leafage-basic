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

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.repository.RolePrivilegesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.BDDMockito.given;

/**
 * role privilege service test
 *
 * @author liwenqiang 2021-06-14 11:10
 **/
@ExtendWith(MockitoExtension.class)
class RolePrivilegeServiceImplTest {

    @Mock
    private RolePrivilegesRepository rolePrivilegesRepository;

    @InjectMocks
    private RolePrivilegesServiceImpl roleComponentsService;

    @Test
    void privileges() {
        given(this.rolePrivilegesRepository.findByRoleId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(RolePrivileges.class)));

        StepVerifier.create(roleComponentsService.privileges(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void roles() {
        given(this.rolePrivilegesRepository.findByPrivilegeId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(RolePrivileges.class)));

        StepVerifier.create(roleComponentsService.roles(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        given(this.rolePrivilegesRepository.save(Mockito.any(RolePrivileges.class))).willReturn(Mono.just(Mockito.mock(RolePrivileges.class)));

        StepVerifier.create(roleComponentsService.relation(1L, Collections.singleton(2L)))
                .expectNextCount(1).verifyComplete();
    }
}