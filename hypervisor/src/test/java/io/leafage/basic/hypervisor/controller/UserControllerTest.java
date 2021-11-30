package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.document.UserGroup;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserDetailVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import java.util.Collections;
import java.util.List;
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

    @MockBean
    private UserGroupService userGroupService;

    @MockBean
    private UserRoleService userRoleService;

    @MockBean
    private AuthorityService authorityService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        UserVO userVO = new UserVO();
        userVO.setUsername("little3201");
        given(this.userService.retrieve(0, 2)).willReturn(Flux.just(userVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/user").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(UserVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.userService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/user").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        UserVO userVO = new UserVO();
        userVO.setNickname("布吉岛");
        userVO.setPhone("18710339898");
        userVO.setEmail("test@test.com");
        given(this.userService.fetch(Mockito.anyString())).willReturn(Mono.just(userVO));

        webTestClient.get().uri("/user/{username}", "little3201").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.nickname").isEqualTo("布吉岛");
    }

    @Test
    void fetch_error() {
        given(this.userService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/{username}", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void details() {
        UserDetailVO userDetailVO = new UserDetailVO();
        userDetailVO.setUsername("little3201");
        given(this.userService.details(Mockito.anyString())).willReturn(Mono.just(userDetailVO));

        webTestClient.get().uri("/user/{username}/details", "little3201").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.username").isEqualTo("little3201");
    }

    @Test
    void details_error() {
        given(this.userService.details(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/{username}/details", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void count() {
        given(this.userService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/user/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.userService.count()).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/count").exchange().expectStatus().isNoContent();
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
    void create() {
        UserVO userVO = new UserVO();
        userVO.setUsername("little3201");
        given(this.userService.create(Mockito.any(UserDTO.class))).willReturn(Mono.just(userVO));

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        webTestClient.post().uri("/user").bodyValue(userDTO).exchange().expectStatus().isCreated()
                .expectBody().jsonPath("$.username").isEqualTo("little3201");
    }

    @Test
    void create_error() {
        given(this.userService.create(Mockito.any(UserDTO.class))).willThrow(new RuntimeException());

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        webTestClient.post().uri("/user").bodyValue(userDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        UserVO userVO = new UserVO();
        userVO.setEmail("test@test.com");
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willReturn(Mono.just(userVO));

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");
        webTestClient.put().uri("/user/{username}", "test").bodyValue(userDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.email").isNotEmpty();
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

    @Test
    void groups() {
        given(this.userGroupService.groups(Mockito.anyString())).willReturn(Mono.just(List.of("21612OL34")));

        webTestClient.get().uri("/user/{username}/group", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);
    }

    @Test
    void groups_error() {
        given(this.userGroupService.groups(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/{username}/group", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void group() {
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(new ObjectId());
        given(this.userGroupService.relation(Mockito.anyString(), Mockito.anySet())).willReturn(Flux.just(userGroup));

        webTestClient.patch().uri("/user/{username}/group", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().isAccepted()
                .expectBodyList(UserGroup.class);
    }

    @Test
    void group_error() {
        given(this.userGroupService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        webTestClient.patch().uri("/user/{username}/group", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    void roles() {
        given(this.userRoleService.roles(Mockito.anyString())).willReturn(Mono.just(List.of("21612OL34")));

        webTestClient.get().uri("/user/{username}/role", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);
    }

    @Test
    void roles_error() {
        given(this.userRoleService.roles(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/{username}/role", "little3201").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void role() {
        UserRole userRole = new UserRole();
        userRole.setRoleId(new ObjectId());
        given(this.userRoleService.relation(Mockito.anyString(), Mockito.anySet())).willReturn(Flux.just(userRole));

        webTestClient.patch().uri("/user/{username}/role", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().isAccepted()
                .expectBodyList(UserRole.class);
    }

    @Test
    void role_error() {
        given(this.userRoleService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        webTestClient.patch().uri("/user/{username}/role", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    void authority() {
        TreeNode treeNode = new TreeNode("21612OL31", "Dashboard");
        given(this.authorityService.authorities(Mockito.anyString())).willReturn(Flux.just(treeNode));

        webTestClient.get().uri("/user/{username}/authority", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleVO.class);
    }

    @Test
    void authority_error() {
        given(this.authorityService.authorities(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/user/{username}/authority", "little3201").exchange()
                .expectStatus().isNoContent();
    }
}