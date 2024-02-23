/*
 *  Copyright 2018-2024 little3201.
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

import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;

import java.time.Instant;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

/**
 * privilege 接口测试类
 *
 * @author liwenqiang 2021-06-19 10:00
 */
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebFluxTest(PrivilegeController.class)
class PrivilegeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RolePrivilegesService rolePrivilegesService;

    @MockBean
    private PrivilegeService privilegeService;

    private PrivilegeDTO privilegeDTO;
    private PrivilegeVO privilegeVO;
    private RolePrivileges rolePrivileges;

    @BeforeEach
    void init() {
        privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setName("test");
        privilegeDTO.setType('M');
        privilegeDTO.setIcon("add");
        privilegeDTO.setSuperiorId(1L);
        privilegeDTO.setDescription("privilege");

        privilegeVO = new PrivilegeVO();
        privilegeVO.setName("test");
        privilegeVO.setIcon("add");
        privilegeVO.setPath("/test");
        privilegeVO.setSuperior("superior");
        privilegeVO.setType('M');
        privilegeVO.setLastModifiedDate(Instant.now());

        rolePrivileges = new RolePrivileges();
        rolePrivileges.setRoleId(1L);
        rolePrivileges.setPrivilegeId(1L);
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<PrivilegeVO> voPage = new PageImpl<>(List.of(privilegeVO), pageable, 1L);
        given(this.privilegeService.retrieve(0, 2)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/privileges").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.privilegeService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/privileges").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void tree() {
        given(this.privilegeService.tree()).willReturn(Mono.just(List.of(new TreeNode(1L, "test"))));

        webTestClient.get().uri("/privileges/tree").exchange()
                .expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void tree_error() {
        given(this.privilegeService.tree()).willThrow(new RuntimeException());

        webTestClient.get().uri("/privileges/tree").exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.privilegeService.fetch(Mockito.anyLong())).willReturn(Mono.just(privilegeVO));

        webTestClient.get().uri("/privileges/{id}", 1L).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.privilegeService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/privileges/{id}", 1L).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.privilegeService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/privileges/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.privilegeService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/privileges/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.privilegeService.create(Mockito.any(PrivilegeDTO.class))).willReturn(Mono.just(privilegeVO));

        webTestClient.mutateWith(csrf()).post().uri("/privileges").bodyValue(privilegeDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.privilegeService.create(Mockito.any(PrivilegeDTO.class))).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).post().uri("/privileges").bodyValue(privilegeDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        given(this.privilegeService.modify(Mockito.anyLong(), Mockito.any(PrivilegeDTO.class))).willReturn(Mono.just(privilegeVO));

        webTestClient.mutateWith(csrf()).put().uri("/privileges/{id}", 1L).bodyValue(privilegeDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.privilegeService.modify(Mockito.anyLong(), Mockito.any(PrivilegeDTO.class))).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).put().uri("/privileges/{id}", 1L).bodyValue(privilegeDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void roles() {
        given(this.rolePrivilegesService.roles(Mockito.anyLong())).willReturn(Mono.just(List.of(rolePrivileges)));

        webTestClient.get().uri("/privileges/{id}/roles", 1L).exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleMembers.class);
    }

    @Test
    void roles_error() {
        given(this.rolePrivilegesService.roles(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/privileges/{id}/roles", 1L).exchange()
                .expectStatus().isNoContent();
    }
}