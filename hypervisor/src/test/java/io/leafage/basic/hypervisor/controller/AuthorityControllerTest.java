package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
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
import static org.mockito.BDDMockito.given;

/**
 * authority接口测试类
 *
 * @author liwenqiang 2021/6/19 10:00
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(AuthorityController.class)
class AuthorityControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RoleAuthorityService roleAuthorityService;

    @MockBean
    private AuthorityService authorityService;

    @Test
    void retrieve() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.retrieve(0, 2)).willReturn(Flux.just(authorityVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/authority").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void tree() {
        TreeNode treeNode = new TreeNode("21612OL34", "test");
        given(this.authorityService.tree()).willReturn(Flux.just(treeNode));

        webTestClient.get().uri("/authority/tree").exchange()
                .expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void fetch() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.fetch(Mockito.anyString())).willReturn(Mono.just(authorityVO));

        webTestClient.get().uri("/authority/{code}", "21612OL34").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void count() {
        given(this.authorityService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/authority/count").exchange().expectStatus().isOk();
    }

    @Test
    void exist() {
        given(this.authorityService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/authority/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void create() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.create(Mockito.any(AuthorityDTO.class))).willReturn(Mono.just(authorityVO));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        webTestClient.post().uri("/authority").bodyValue(authorityDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.modify(Mockito.anyString(), Mockito.any(AuthorityDTO.class))).willReturn(Mono.just(authorityVO));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        webTestClient.put().uri("/authority/{code}", "21612OL34").bodyValue(authorityDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void roles() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleAuthorityService.roles(Mockito.anyString())).willReturn(Flux.just(roleVO));

        webTestClient.get().uri("/authority/{code}/role", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleVO.class);
    }
}