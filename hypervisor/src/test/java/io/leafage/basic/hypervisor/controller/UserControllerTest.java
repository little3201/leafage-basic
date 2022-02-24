package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
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


    @Test
    void fetch() {
        UserVO userVO = new UserVO();
        userVO.setUsername("leafage");
        userVO.setBirthday(LocalDate.now());
        userVO.setPosition("engineer");
        given(this.userService.fetch(Mockito.anyString())).willReturn(Mono.just(userVO));

        webTestClient.get().uri("/user/{username}", "leafage").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.username").isEqualTo("leafage");
    }

    @Test
    void fetch_error() {
        given(this.userService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/{username}", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.userService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri("/user/{username}/exist", "little3201").exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.userService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/{username}/exist", "little3201").exchange().expectStatus().isNoContent();
    }

    @Test
    void modify() {
        UserVO userVO = new UserVO();
        userVO.setBirthday(LocalDate.now());
        userVO.setDegree("postgraduate");
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willReturn(Mono.just(userVO));

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        webTestClient.put().uri("/user/{username}", "test").bodyValue(userDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.degree").isNotEmpty();
    }

    @Test
    void modify_error() {
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willThrow(new RuntimeException());

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        webTestClient.put().uri("/user/{username}", "test").bodyValue(userDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.userService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/user/{username}", "little3201").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.userService.remove(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/user/{username}", "little3201").exchange()
                .expectStatus().is4xxClientError();
    }

}