package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.document.UserGroup;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
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

import java.util.Collections;

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
    void fetchDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername("little3201");
        given(this.userService.fetchDetails(Mockito.anyString())).willReturn(Mono.just(userDetails));

        webTestClient.get().uri("/user/{username}/details", "little3201").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.username").isEqualTo("little3201");
    }

    @Test
    void count() {
        given(this.userService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/user/count").exchange().expectStatus().isOk();
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
    void remove() {
        given(this.userService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/user/{username}", "little3201").exchange()
                .expectStatus().isOk();
    }

    @Test
    void groups() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.userGroupService.groups(Mockito.anyString())).willReturn(Flux.just(groupVO));

        webTestClient.get().uri("/user/{username}/group", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(GroupVO.class);
    }

    @Test
    void relationGroup() {
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(new ObjectId());
        given(this.userGroupService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Flux.just(userGroup));

        webTestClient.patch().uri("/user/{username}/group", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().isAccepted()
                .expectBodyList(UserGroup.class);
    }

    @Test
    void roles() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.userRoleService.roles(Mockito.anyString())).willReturn(Flux.just(roleVO));

        webTestClient.get().uri("/user/{username}/role", "little3201").exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleVO.class);
    }

    @Test
    void relationRole() {
        UserRole userRole = new UserRole();
        userRole.setRoleId(new ObjectId());
        given(this.userRoleService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Flux.just(userRole));

        webTestClient.patch().uri("/user/{username}/role", "little3201")
                .bodyValue(Collections.singleton("21612OL34"))
                .exchange().expectStatus().isAccepted()
                .expectBodyList(UserRole.class);
    }
}