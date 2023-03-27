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

package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

/**
 * user接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    private UserDTO userDTO;
    private UserVO userVO;

    @BeforeEach
    void init() {
        userDTO = new UserDTO();
        userDTO.setUsername("test");

        userVO = new UserVO();
        userVO.setUsername("test");
        userVO.setAccountExpiresAt(LocalDateTime.now());
        userVO.setNickname("admin");
    }

    @Test
    void fetch() {
        given(this.userService.fetch(Mockito.anyString())).willReturn(Mono.just(userVO));

        webTestClient.get().uri("/users/{username}", "test").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.username").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.userService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/users/{username}", "test").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.userService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri("/users/{username}/exist", "test").exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.userService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/users/{username}/exist", "test").exchange().expectStatus().isNoContent();
    }

    @Test
    void modify() {
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willReturn(Mono.just(userVO));

        webTestClient.put().uri("/users/{username}", "test").bodyValue(userDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.username").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willThrow(new RuntimeException());

        webTestClient.put().uri("/users/{username}", "test").bodyValue(userDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.userService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/users/{username}", "test").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.userService.remove(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/users/{username}", "test").exchange()
                .expectStatus().is4xxClientError();
    }

}