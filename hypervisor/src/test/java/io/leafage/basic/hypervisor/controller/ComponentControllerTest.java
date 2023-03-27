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

import io.leafage.basic.hypervisor.domain.RoleComponents;
import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.dto.ComponentDTO;
import io.leafage.basic.hypervisor.service.ComponentService;
import io.leafage.basic.hypervisor.service.RoleComponentsService;
import io.leafage.basic.hypervisor.vo.ComponentVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.BeforeEach;
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
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * authority接口测试类
 *
 * @author liwenqiang 2021/6/19 10:00
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(ComponentController.class)
class ComponentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RoleComponentsService roleComponentsService;

    @MockBean
    private ComponentService componentService;

    private ComponentDTO componentDTO;
    private ComponentVO componentVO;
    private RoleComponents roleComponents;

    @BeforeEach
    void init() {
        componentDTO = new ComponentDTO();
        componentDTO.setComponentName("test");
        componentDTO.setType('M');
        componentDTO.setIcon("add");

        componentVO = new ComponentVO();
        componentVO.setComponentName("test");
        componentVO.setIcon("add");
        componentVO.setPath("/test");
        componentVO.setSuperiorId(1L);
        componentVO.setType('M');
        componentVO.setModifyTime(LocalDateTime.now());

        roleComponents = new RoleComponents();
        roleComponents.setRoleId(1L);
        roleComponents.setComponentId(1L);
    }

    @Test
    void retrieve() {
        Page<ComponentVO> voPage = new PageImpl<>(List.of(componentVO));
        given(this.componentService.retrieve(0, 2)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/components").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.componentService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/components").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void tree() {
        given(this.componentService.tree()).willReturn(Mono.just(List.of(new TreeNode(1L, "test"))));

        webTestClient.get().uri("/components/tree").exchange()
                .expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void tree_error() {
        given(this.componentService.tree()).willThrow(new RuntimeException());

        webTestClient.get().uri("/components/tree").exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.componentService.fetch(Mockito.anyLong())).willReturn(Mono.just(componentVO));

        webTestClient.get().uri("/components/{id}", 1L).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.componentName").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.componentService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/components/{id}", 1L).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.componentService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/components/exist")
                .queryParam("componentName", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.componentService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/components/exist")
                .queryParam("componentName", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.componentService.create(Mockito.any(ComponentDTO.class))).willReturn(Mono.just(componentVO));

        webTestClient.post().uri("/components").bodyValue(componentDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.componentName").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.componentService.create(Mockito.any(ComponentDTO.class))).willThrow(new RuntimeException());

        webTestClient.post().uri("/components").bodyValue(componentDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        given(this.componentService.modify(Mockito.anyLong(), Mockito.any(ComponentDTO.class))).willReturn(Mono.just(componentVO));

        webTestClient.put().uri("/components/{id}", 1L).bodyValue(componentDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.componentName").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.componentService.modify(Mockito.anyLong(), Mockito.any(ComponentDTO.class))).willThrow(new RuntimeException());

        webTestClient.put().uri("/components/{id}", 1L).bodyValue(componentDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void roles() {
        given(this.roleComponentsService.roles(Mockito.anyLong())).willReturn(Mono.just(List.of(roleComponents)));

        webTestClient.get().uri("/components/{id}/roles", 1L).exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleMembers.class);
    }

    @Test
    void roles_error() {
        given(this.roleComponentsService.roles(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/components/{id}/roles", 1L).exchange()
                .expectStatus().isNoContent();
    }
}