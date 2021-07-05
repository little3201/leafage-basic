package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.service.UserRoleService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
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
import top.leafage.common.basic.TreeNode;
import java.util.Collections;
import static org.mockito.BDDMockito.given;

/**
 * role接口测试类
 *
 * @author liwenqiang 2021/6/19 10:00
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserRoleService userRoleService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private RoleAuthorityService roleAuthorityService;

    @Test
    void retrieve() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.retrieve(0, 2)).willReturn(Flux.just(roleVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/role").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void tree() {
        TreeNode treeNode = new TreeNode("21612OL34", "test");
        given(this.roleService.tree()).willReturn(Flux.just(treeNode));

        webTestClient.get().uri("/role/tree").exchange()
                .expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void fetch() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.fetch(Mockito.anyString())).willReturn(Mono.just(roleVO));

        webTestClient.get().uri("/role/{code}", "21612OL34").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void count() {
        given(this.roleService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/role/count").exchange().expectStatus().isOk();
    }

    @Test
    void create() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willReturn(Mono.just(roleVO));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        webTestClient.post().uri("/role").bodyValue(roleDTO).exchange().expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.modify(Mockito.anyString(), Mockito.any(RoleDTO.class))).willReturn(Mono.just(roleVO));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        webTestClient.put().uri("/role/{code}", "21612OL34").bodyValue(roleDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void users() {
        UserVO userVO = new UserVO();
        userVO.setUsername("little3201");
        given(this.userRoleService.users(Mockito.anyString())).willReturn(Flux.just(userVO));

        webTestClient.get().uri("/role/{code}/user", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(UserRole.class);
    }

    @Test
    void authorities() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.roleAuthorityService.authorities(Mockito.anyString())).willReturn(Flux.just(authorityVO));

        webTestClient.get().uri("/role/{code}/authority", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(UserRole.class);
    }

    @Test
    void relation() {
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setAuthorityId(new ObjectId());
        given(this.roleAuthorityService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Flux.just(roleAuthority));

        webTestClient.patch().uri("/role/{code}/authority", "21612OL34")
                .bodyValue(Collections.singleton("21612OP34"))
                .exchange().expectStatus().isAccepted()
                .expectBodyList(UserRole.class);
    }
}