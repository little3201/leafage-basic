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
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

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
        when(userService.fetch(Mockito.anyString())).thenReturn(Mono.just(userVO));
        webTestClient.get().uri("/user/{username}", Mockito.anyString())
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectBody().jsonPath("$.nickname").isNotEmpty();
    }
}