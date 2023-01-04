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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;

import java.util.List;

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
        Page<AuthorityVO> voPage = new PageImpl<>(List.of(authorityVO));
        given(this.authorityService.retrieve(0, 2)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/authorities").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.authorityService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/authorities").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void tree() {
        TreeNode treeNode = new TreeNode("21612OL34", "test");
        given(this.authorityService.tree()).willReturn(Mono.just(List.of(treeNode)));

        webTestClient.get().uri("/authorities/tree").exchange()
                .expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void tree_error() {
        given(this.authorityService.tree()).willThrow(new RuntimeException());

        webTestClient.get().uri("/authorities/tree").exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.fetch(Mockito.anyString())).willReturn(Mono.just(authorityVO));

        webTestClient.get().uri("/authorities/{code}", "21612OL34").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.authorityService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/authorities/{code}", "21612OL34").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.authorityService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/authorities/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.authorityService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/authorities/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.create(Mockito.any(AuthorityDTO.class))).willReturn(Mono.just(authorityVO));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        webTestClient.post().uri("/authorities").bodyValue(authorityDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.authorityService.create(Mockito.any(AuthorityDTO.class))).willThrow(new RuntimeException());

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        webTestClient.post().uri("/authorities").bodyValue(authorityDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.modify(Mockito.anyString(), Mockito.any(AuthorityDTO.class))).willReturn(Mono.just(authorityVO));

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        webTestClient.put().uri("/authorities/{code}", "21612OL34").bodyValue(authorityDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.authorityService.modify(Mockito.anyString(), Mockito.any(AuthorityDTO.class))).willThrow(new RuntimeException());

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        webTestClient.put().uri("/authorities/{code}", "21612OL34").bodyValue(authorityDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void roles() {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleAuthorityService.roles(Mockito.anyString())).willReturn(Flux.just(roleVO));

        webTestClient.get().uri("/authorities/{code}/roles", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleVO.class);
    }

    @Test
    void roles_error() {
        given(this.roleAuthorityService.roles(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/authorities/{code}/roles", "21612OL34").exchange()
                .expectStatus().isNoContent();
    }
}