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
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.RoleMembersService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import io.leafage.basic.hypervisor.service.RoleService;
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

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

/**
 * role接口测试类
 *
 * @author liwenqiang 2021-06-19 10:00
 */
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebFluxTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RoleMembersService roleMembersService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private RolePrivilegesService rolePrivilegesService;

    private RoleDTO roleDTO;
    private RoleVO roleVO;
    private RolePrivileges rolePrivileges;
    private RoleMembers roleMembers;

    @BeforeEach
    void init() {
        roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setSuperiorId(1L);
        roleDTO.setName("role");

        roleVO = new RoleVO();
        roleVO.setName("test");
        roleVO.setLastModifiedDate(Instant.now());
        roleVO.setDescription("role");

        rolePrivileges = new RolePrivileges();
        rolePrivileges.setRoleId(1L);
        rolePrivileges.setPrivilegeId(1L);

        roleMembers = new RoleMembers();
        roleMembers.setRoleId(1L);
        roleMembers.setUsername("test");
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<RoleVO> voPage = new PageImpl<>(List.of(roleVO), pageable, 1L);
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
    void fetch() {
        given(this.roleService.fetch(Mockito.anyLong())).willReturn(Mono.just(roleVO));

        webTestClient.get().uri("/roles/{id}", 1L).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.roleService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/roles/{id}", 1L).exchange().expectStatus().isNoContent();
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
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willReturn(Mono.just(roleVO));

        webTestClient.mutateWith(csrf()).post().uri("/roles").bodyValue(roleDTO).exchange().expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).post().uri("/roles").bodyValue(roleDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        given(this.roleService.modify(Mockito.anyLong(), Mockito.any(RoleDTO.class))).willReturn(Mono.just(roleVO));

        webTestClient.mutateWith(csrf()).put().uri("/roles/{id}", 1L).bodyValue(roleDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.roleService.modify(Mockito.anyLong(), Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).put().uri("/roles/{id}", 1L).bodyValue(roleDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void members() {
        given(this.roleMembersService.members(Mockito.anyLong())).willReturn(Mono.just(List.of(roleMembers)));

        webTestClient.get().uri("/roles/{id}/members", 1L).exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleMembers.class);
    }

    @Test
    void members_error() {
        given(this.roleMembersService.members(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/roles/{id}/members", 1L).exchange().expectStatus().isNoContent();
    }

    @Test
    void privileges() {
        given(this.rolePrivilegesService.privileges(Mockito.anyLong())).willReturn(Mono.just(List.of(rolePrivileges)));

        webTestClient.get().uri("/roles/{id}/privileges", 1L).exchange()
                .expectStatus().isOk()
                .expectBodyList(RolePrivileges.class);
    }

    @Test
    void privileges_error() {
        given(this.rolePrivilegesService.privileges(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/roles/{id}/privileges", 1L).exchange().expectStatus().isNoContent();
    }

    @Test
    void relation() {
        given(this.rolePrivilegesService.relation(Mockito.anyLong(), Mockito.anySet()))
                .willReturn(Mono.just(Boolean.TRUE));

        webTestClient.mutateWith(csrf()).patch().uri("/roles/{id}/privileges", 1L)
                .bodyValue(Collections.singleton(1L))
                .exchange().expectStatus().isAccepted();
    }

    @Test
    void relation_error() {
        given(this.rolePrivilegesService.relation(Mockito.anyLong(), Mockito.anySet())).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).patch().uri("/roles/{id}/privileges", 1L)
                .bodyValue(Collections.singleton(1L))
                .exchange().expectStatus().is4xxClientError();
    }
}