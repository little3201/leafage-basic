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

package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.service.PostService;
import io.leafage.basic.assets.vo.PostVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * posts controller test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(PostController.class)
class PostControllerTest {

    @MockBean
    private PostService postService;

    @Autowired
    private WebTestClient webTestClient;

    private PostDTO postDTO;
    private PostVO postVO;

    @BeforeEach
    void init() {
        // 构造请求对象
        postDTO = new PostDTO();
        postDTO.setTitle("test");
        postDTO.setTags(Collections.singleton("java"));
        postDTO.setCover("../test.jpg");

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContext("内容信息");
        postDTO.setContent(contentBO);

        postVO = new PostVO();
        postVO.setTitle(postDTO.getTitle());
        postVO.setTags(postDTO.getTags());
        postVO.setCover(postDTO.getCover());
        postVO.setCategoryId(2L);
        postVO.setContent(contentBO);
        postVO.setModifyTime(LocalDateTime.now());
    }

    @Test
    void retrieve() {
        Page<PostVO> page = new PageImpl<>(List.of(postVO));
        given(this.postService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyLong()))
                .willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).build())
                .exchange().expectStatus().isOk().expectBodyList(PostVO.class);
    }

    @Test
    void retrieve_category() {
        Page<PostVO> page = new PageImpl<>(List.of(postVO));
        given(this.postService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyLong()))
                .willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("categoryId", 1L).build())
                .exchange().expectStatus().isOk().expectBodyList(PostVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.postService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyLong()))
                .willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("categoryId", 1L)
                        .queryParam("sort", "").build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.postService.fetch(Mockito.anyLong())).willReturn(Mono.just(postVO));

        webTestClient.get().uri("/posts/{id}", 1).exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.postService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{id}", 1).exchange().expectStatus().isNoContent();
    }

    @Test
    void search() {
        given(this.postService.search(Mockito.anyString())).willReturn(Flux.just(postVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/search")
                        .queryParam("keyword", "test").build()).exchange()
                .expectStatus().isOk()
                .expectBodyList(PostVO.class);
    }

    @Test
    void search_error() {
        given(this.postService.search(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/search")
                        .queryParam("keyword", "test").build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.postService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/exist")
                        .queryParam("title", "test").build()).exchange()
                .expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.postService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.postService.create(Mockito.any(PostDTO.class))).willReturn(Mono.just(postVO));

        webTestClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.postService.create(Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        webTestClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        given(this.postService.modify(Mockito.anyLong(), Mockito.any(PostDTO.class))).willReturn(Mono.just(postVO));

        webTestClient.put().uri("/posts/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.postService.modify(Mockito.anyLong(), Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        webTestClient.put().uri("/posts/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange().expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.postService.remove(Mockito.anyLong())).willReturn(Mono.empty());

        webTestClient.delete().uri("/posts/{id}", 1).exchange().expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.postService.remove(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/posts/{id}", 1).exchange().expectStatus().is4xxClientError();
    }
}