package io.leafage.basic.hypervisor.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * 用户接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void fetchDetails() {
        webTestClient.get().uri("/user/{username}", "little3201")
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectBody().jsonPath("nickname").isNotEmpty();
    }
}