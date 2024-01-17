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

import io.leafage.basic.hypervisor.domain.GroupRoles;
import io.leafage.basic.hypervisor.repository.GroupRolesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.BDDMockito.given;

/**
 * group roles service test
 *
 * @author liwenqiang 2024/01/17 21:18
 **/
@ExtendWith(MockitoExtension.class)
class GroupRolesServiceImplTest {

    @Mock
    private GroupRolesRepository groupRolesRepository;

    @InjectMocks
    private GroupRolesServiceImpl groupRolesService;

    @Test
    void roles() {
        given(this.groupRolesRepository.findByGroupId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(GroupRoles.class)));

        StepVerifier.create(groupRolesService.roles(Mockito.anyLong())).expectNextCount(1).verifyComplete();
    }

    @Test
    void groups() {
        given(this.groupRolesRepository.findByRoleId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(GroupRoles.class)));

        StepVerifier.create(groupRolesService.groups(Mockito.anyLong())).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        given(this.groupRolesRepository.saveAll(Mockito.anyCollection())).willReturn(Flux.just(Mockito.mock(GroupRoles.class)));

        StepVerifier.create(groupRolesService.relation(Mockito.anyLong(), Set.of(1L)))
                .expectNextCount(1).verifyComplete();
    }
}