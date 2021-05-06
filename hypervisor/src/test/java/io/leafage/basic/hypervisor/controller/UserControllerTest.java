/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;


import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

/**
 * 用户接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void fetch() {
        UserVO userVO = new UserVO();
        userVO.setNickname("布吉岛");
        userVO.setPhone("18710339898");
        userVO.setEmail("test@test.com");
        given(this.userService.fetch(Mockito.anyString())).willReturn(Mono.just(userVO));
        webTestClient.get().uri("/user/{username}", "little3201").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.nickname").isNotEmpty();
    }
}