/*
 *  Copyright 2018-2024 the original author or authors.
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

import io.leafage.basic.hypervisor.domain.GroupMembers;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.GroupMembersService;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
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

import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * group接口测试类
 *
 * @author liwenqiang 2021-06-19 10:00
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GroupMembersService groupMembersService;

    @MockBean
    private GroupService groupService;

    private GroupDTO groupDTO;
    private GroupVO groupVO;
    private GroupMembers groupMembers;

    @BeforeEach
    void init() {
        groupVO = new GroupVO();
        groupVO.setGroupName("test");
        groupVO.setPrincipal("test");

        groupDTO = new GroupDTO();
        groupDTO.setGroupName("test");
        groupDTO.setPrincipal("Test");

        groupMembers = new GroupMembers();
        groupMembers.setGroupId(1L);
        groupMembers.setUsername("test");
    }

    @Test
    void retrieve_page() {
        Page<GroupVO> voPage = new PageImpl<>(List.of(groupVO));
        given(this.groupService.retrieve(0, 2)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/groups").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.groupService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/groups").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.groupService.fetch(Mockito.anyLong())).willReturn(Mono.just(groupVO));

        webTestClient.get().uri("/groups/{id}", 1L).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.groupName").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.groupService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/groups/{id}", 1L).exchange().expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.groupService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/groups/exist")
                .queryParam("groupName", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.groupService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/groups/exist")
                .queryParam("groupName", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willReturn(Mono.just(groupVO));

        webTestClient.post().uri("/groups").bodyValue(groupDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.groupName").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        webTestClient.post().uri("/groups").bodyValue(groupDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        given(this.groupService.modify(Mockito.anyLong(), Mockito.any(GroupDTO.class))).willReturn(Mono.just(groupVO));

        webTestClient.put().uri("/groups/{id}", 1L).bodyValue(groupDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.groupName").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.groupService.modify(Mockito.anyLong(), Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        webTestClient.put().uri("/groups/{id}", 1L).bodyValue(groupDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.groupService.remove(Mockito.anyLong())).willReturn(Mono.empty());

        webTestClient.delete().uri("/groups/{id}", 1L).exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.groupService.remove(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/groups/{id}", 1L).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void members() {
        given(this.groupMembersService.members(Mockito.anyLong())).willReturn(Mono.just(List.of(groupMembers)));

        webTestClient.get().uri("/groups/{id}/members", 1L).exchange()
                .expectStatus().isOk()
                .expectBodyList(GroupMembers.class);
    }

    @Test
    void members_error() {
        given(this.groupMembersService.members(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/groups/{id}/members", 1L).exchange()
                .expectStatus().isNoContent();
    }
}