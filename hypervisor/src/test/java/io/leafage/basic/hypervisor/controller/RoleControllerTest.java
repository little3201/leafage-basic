package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.document.AccountRole;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;

import java.util.Collections;
import java.util.List;

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
    private AccountRoleService accountRoleService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private RoleAuthorityService roleAuthorityService;

    @Test
    void retrieve() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        Page<RoleVO> voPage = new PageImpl<>(List.of(roleVO));
        given(this.roleService.retrieve(0, 2)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/roles").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.roleService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/roles").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void tree() {
        TreeNode treeNode = new TreeNode("21612OL34", "test");
        given(this.roleService.tree()).willReturn(Flux.just(treeNode));

        webTestClient.get().uri("/roles/tree").exchange()
                .expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void tree_error() {
        given(this.roleService.tree()).willThrow(new RuntimeException());

        webTestClient.get().uri("/roles/tree").exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.fetch(Mockito.anyString())).willReturn(Mono.just(roleVO));

        webTestClient.get().uri("/roles/{code}", "21612OL34").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.roleService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/roles/{code}", "21612OL34").exchange().expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.roleService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/roles/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.roleService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/roles/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willReturn(Mono.just(roleVO));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setAlias("Test");
        webTestClient.post().uri("/roles").bodyValue(roleDTO).exchange().expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setAlias("Test");
        webTestClient.post().uri("/roles").bodyValue(roleDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.modify(Mockito.anyString(), Mockito.any(RoleDTO.class))).willReturn(Mono.just(roleVO));

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setAlias("Test");
        webTestClient.put().uri("/roles/{code}", "21612OL34").bodyValue(roleDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.roleService.modify(Mockito.anyString(), Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setAlias("Test");
        webTestClient.put().uri("/roles/{code}", "21612OL34").bodyValue(roleDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void accounts() {
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername("little3201");
        given(this.accountRoleService.accounts(Mockito.anyString())).willReturn(Flux.just(accountVO));

        webTestClient.get().uri("/roles/{code}/account", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountRole.class);
    }

    @Test
    void accounts_error() {
        given(this.accountRoleService.accounts(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/roles/{code}/account", "21612OL34").exchange().expectStatus().isNoContent();
    }

    @Test
    void authorities() {
        given(this.roleAuthorityService.authorities(Mockito.anyString())).willReturn(Mono.just(List.of("21612OL34")));

        webTestClient.get().uri("/roles/{code}/authority", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);
    }

    @Test
    void authorities_error() {
        given(this.roleAuthorityService.authorities(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/roles/{code}/authority", "21612OL34").exchange().expectStatus().isNoContent();
    }

    @Test
    void relation() {
        given(this.roleAuthorityService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Mono.just(true));

        webTestClient.patch().uri("/roles/{code}/authority", "21612OL34")
                .bodyValue(Collections.singleton("21612OP34"))
                .exchange().expectStatus().isAccepted();
    }

    @Test
    void relation_error() {
        given(this.roleAuthorityService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        webTestClient.patch().uri("/roles/{code}/authority", "21612OL34")
                .bodyValue(Collections.singleton("21612OP34"))
                .exchange().expectStatus().is4xxClientError();
    }
}