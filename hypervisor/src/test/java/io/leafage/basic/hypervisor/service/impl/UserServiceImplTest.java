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

import io.leafage.basic.hypervisor.domain.User;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.repository.UserRepository;
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
 * user接口测试
 *
 * @author liwenqiang 2019-01-29 17:10
 **/
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void retrieve() {
        given(this.userRepository.findByEnabledTrue(Mockito.any(PageRequest.class))).willReturn(Flux.just(Mockito.mock(User.class)));

        given(this.userRepository.count()).willReturn(Mono.just(2L));

        StepVerifier.create(userService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(User.class)));
        StepVerifier.create(userService.fetch("little3201")).expectNextCount(1).verifyComplete();
    }

    /**
     * 测试新增user
     */
    @Test
    void create() {
        given(this.userRepository.save(Mockito.any(User.class))).willReturn(Mono.just(Mockito.mock(User.class)));
        StepVerifier.create(userService.create(Mockito.mock(UserDTO.class))).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.userRepository.existsByUsername(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(userService.exist("little3201")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void modify() {
        given(this.userRepository.getByUsername(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(User.class)));

        given(this.userRepository.save(Mockito.any(User.class))).willReturn(Mono.just(Mockito.mock(User.class)));

        UserDTO userDTO = new UserDTO();
        StepVerifier.create(userService.modify("little3201", userDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        given(this.userRepository.deleteByUsername(Mockito.anyString())).willReturn(Mono.empty());

        StepVerifier.create(userService.remove("little3201")).verifyComplete();
    }
}