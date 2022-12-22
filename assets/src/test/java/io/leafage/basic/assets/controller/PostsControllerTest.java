/*
 *  Copyright 2018-2022 the original author or authors.
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

import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostVO;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * posts controller test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(PostsController.class)
class PostsControllerTest {

    @MockBean
    private PostsService postsService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        Page<PostVO> page = new PageImpl<>(List.of(postVO));
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2").build())
                .exchange().expectStatus().isOk().expectBodyList(PostVO.class);
    }

    @Test
    void retrieve_category() {
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        Page<PostVO> page = new PageImpl<>(List.of(postVO));
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2").build())
                .exchange().expectStatus().isOk().expectBodyList(PostVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2")
                        .queryParam("sort", "").build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        given(this.postsService.fetch(Mockito.anyString())).willReturn(Mono.just(postVO));

        webTestClient.get().uri("/posts/{code}", "21213G0J2").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.postsService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{code}", "21213G0J2").exchange().expectStatus().isNoContent();
    }

    @Test
    void previous() {
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        given(this.postsService.previous(Mockito.anyString())).willReturn(Mono.just(postVO));

        webTestClient.get().uri("/posts/{code}/previous", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void previous_error() {
        given(this.postsService.previous(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{code}/previous", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void next() {
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        given(this.postsService.next(Mockito.anyString())).willReturn(Mono.just(postVO));

        webTestClient.get().uri("/posts/{code}/next", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void next_error() {
        given(this.postsService.next(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{code}/next", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void search() {
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        given(this.postsService.search(Mockito.anyString())).willReturn(Flux.just(postVO));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/posts/search").queryParam("keyword", "test").build()).exchange()
                .expectStatus().isOk();
    }

    @Test
    void search_error() {
        given(this.postsService.search(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/posts/search").queryParam("keyword", "test").build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.postsService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.postsService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        // 构造返回对象
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        given(this.postsService.create(Mockito.any(PostDTO.class))).willReturn(Mono.just(postVO));

        // 构造请求对象
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("test");
        postDTO.setTags(Collections.singleton("java"));
        postDTO.setCover("../test.jpg");
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContent("内容信息");
        postDTO.setContent(contentBO);
        webTestClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.postsService.create(Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("test");
        postDTO.setTags(Collections.singleton("java"));
        postDTO.setCover("../test.jpg");


        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContent("内容信息");
        postDTO.setContent(contentBO);
        webTestClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        // 构造返回对象
        PostVO postVO = new PostVO();
        postVO.setTitle("test");
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostDTO.class))).willReturn(Mono.just(postVO));

        // 构造请求对象
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("test");
        postDTO.setTags(Collections.singleton("java"));
        postDTO.setCover("../test.jpg");

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContent("内容信息");
        postDTO.setContent(contentBO);
        webTestClient.put().uri("/posts/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange().expectStatus().isAccepted();
    }

    @Test
    void modify_error() {
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("test");
        postDTO.setTags(Collections.singleton("java"));
        postDTO.setCover("../test.jpg");

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContent("内容信息");
        postDTO.setContent(contentBO);
        webTestClient.put().uri("/posts/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postDTO).exchange().expectStatus().isNotModified();
    }
}