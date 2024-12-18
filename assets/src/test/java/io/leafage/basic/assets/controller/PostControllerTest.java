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

package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.service.PostService;
import io.leafage.basic.assets.vo.PostVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

/**
 * posts controller test
 *
 * @author wq li
 */
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebFluxTest(PostController.class)
class PostControllerTest {

    @MockitoBean
    private PostService postService;

    @Autowired
    private WebTestClient webTestClient;

    private PostDTO postDTO;
    private PostVO postVO;

    @BeforeEach
    void setUp() {
        // 构造请求对象
        postDTO = new PostDTO();
        postDTO.setTitle("test");
        postDTO.setCategoryId(1L);
        postDTO.setCover("../test.jpg");
        postDTO.setTags(Collections.singleton("java"));
        postDTO.setContext("内容信息");

        postVO = new PostVO(1L, true, Instant.now());
        postVO.setTitle(postDTO.getTitle());
        postVO.setTags(postDTO.getTags());
        postVO.setCover(postDTO.getCover());
        postVO.setContext(postDTO.getContext());
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<PostVO> page = new PageImpl<>(List.of(postVO), pageable, 1L);
        given(this.postService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean()))
                .willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts")
                        .queryParam("page", 0)
                        .queryParam("size", 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PostVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.postService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean()))
                .willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts")
                        .queryParam("page", 0)
                        .queryParam("size", 2)
                        .queryParam("sortBy", "id")
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.postService.fetch(Mockito.anyLong())).willReturn(Mono.just(postVO));

        webTestClient.get().uri("/posts/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.postService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void search() {
        given(this.postService.search(Mockito.anyString())).willReturn(Flux.just(postVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/search")
                        .queryParam("keyword", "test")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PostVO.class);
    }

    @Test
    void search_error() {
        given(this.postService.search(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/search")
                        .queryParam("keyword", "test")
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exists() {
        given(this.postService.exists(Mockito.anyString(), Mockito.anyLong())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/exists")
                        .queryParam("title", "test")
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.postService.exists(Mockito.anyString(), Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/exists")
                        .queryParam("title", "test")
                        .queryParam("id", 1L)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.postService.create(Mockito.any(PostDTO.class))).willReturn(Mono.just(postVO));

        webTestClient.mutateWith(csrf()).post().uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.postService.create(Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).post().uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        given(this.postService.modify(Mockito.anyLong(), Mockito.any(PostDTO.class))).willReturn(Mono.just(postVO));

        webTestClient.mutateWith(csrf()).put().uri("/posts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.postService.modify(Mockito.anyLong(), Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).put().uri("/posts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO)
                .exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.postService.remove(Mockito.anyLong())).willReturn(Mono.empty());

        webTestClient.mutateWith(csrf()).delete().uri("/posts/{id}", 1)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.postService.remove(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.mutateWith(csrf()).delete().uri("/posts/{id}", 1)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}