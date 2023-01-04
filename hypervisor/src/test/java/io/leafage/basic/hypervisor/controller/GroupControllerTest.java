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

import io.leafage.basic.hypervisor.document.AccountRole;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.GroupVO;
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
 * group接口测试类
 *
 * @author liwenqiang 2021/6/19 10:00
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountGroupService accountGroupService;

    @MockBean
    private GroupService groupService;

    @Test
    void retrieve_page() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
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
    void tree() {
        TreeNode treeNode = new TreeNode("21612OL34", "test");
        given(this.groupService.tree()).willReturn(Mono.just(List.of(treeNode)));

        webTestClient.get().uri("/groups/tree").exchange().expectStatus().isOk().expectBodyList(TreeNode.class);
    }

    @Test
    void tree_error() {
        given(this.groupService.tree()).willThrow(new RuntimeException());

        webTestClient.get().uri("/groups/tree").exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.fetch(Mockito.anyString())).willReturn(Mono.just(groupVO));

        webTestClient.get().uri("/groups/{code}", "21612OL34").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.groupService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/groups/{code}", "21612OL34").exchange().expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.groupService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/groups/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.groupService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/groups/exist")
                .queryParam("name", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willReturn(Mono.just(groupVO));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setAlias("Test");
        webTestClient.post().uri("/groups").bodyValue(groupDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setAlias("Test");
        webTestClient.post().uri("/groups").bodyValue(groupDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.modify(Mockito.anyString(), Mockito.any(GroupDTO.class))).willReturn(Mono.just(groupVO));

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setAlias("Test");
        webTestClient.put().uri("/groups/{code}", "21612OL34").bodyValue(groupDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.groupService.modify(Mockito.anyString(), Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setAlias("Test");
        webTestClient.put().uri("/groups/{code}", "21612OL34").bodyValue(groupDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.groupService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/groups/{code}", "21612OL34").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.groupService.remove(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/groups/{code}", "21612OL34").exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void accounts() {
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername("little3201");
        given(this.accountGroupService.accounts(Mockito.anyString())).willReturn(Flux.just(accountVO));

        webTestClient.get().uri("/groups/{code}/accounts", "21612OL34").exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountRole.class);
    }

    @Test
    void accounts_error() {
        given(this.accountGroupService.accounts(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/groups/{code}/accounts", "21612OL34").exchange()
                .expectStatus().isNoContent();
    }
}