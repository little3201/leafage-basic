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

import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.repository.RoleMembersRepository;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
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
 * user-role接口测试
 *
 * @author liwenqiang 2021/6/14 11:10
 **/
@ExtendWith(MockitoExtension.class)
class RoleMembersServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleMembersRepository roleMembersRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleMembersServiceImpl userRoleService;

    @Test
    void members() {
        given(this.roleMembersRepository.findByRoleId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(RoleMembers.class)));

        StepVerifier.create(userRoleService.members(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void roles() {
        given(this.roleMembersRepository.findByUsername(Mockito.anyString())).willReturn(Flux.just(Mockito.mock(RoleMembers.class)));

        StepVerifier.create(userRoleService.roles("test")).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        given(this.roleMembersRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(RoleMembers.class)));

        StepVerifier.create(userRoleService.relation("test", Collections.singleton(1L)))
                .expectNextCount(1).verifyComplete();
    }
}