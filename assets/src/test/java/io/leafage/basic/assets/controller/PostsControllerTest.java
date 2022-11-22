/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
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
import static org.mockito.Mockito.times;

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
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        Page<PostsVO> page = new PageImpl<>(List.of(postsVO));
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2").build())
                .exchange().expectStatus().isOk().expectBodyList(PostsVO.class);
    }

    @Test
    void retrieve_category() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        Page<PostsVO> page = new PageImpl<>(List.of(postsVO));
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2").build())
                .exchange().expectStatus().isOk().expectBodyList(PostsVO.class);
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
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.fetch(Mockito.anyString())).willReturn(Mono.just(postsVO));

        webTestClient.get().uri("/posts/{code}", "21213G0J2").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.postsService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{code}", "21213G0J2").exchange().expectStatus().isNoContent();
    }

    @Test
    void details() {
        PostsContentVO postsContentVO = new PostsContentVO();
        ContentVO contentVO = new ContentVO();
        contentVO.setContent("test");
        postsContentVO.setContent(contentVO);
        postsContentVO.setTitle("cate");
        given(this.postsService.details(Mockito.anyString())).willReturn(Mono.just(postsContentVO));

        webTestClient.get().uri("/posts/{code}/details", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("cate");
    }

    @Test
    void details_error() {
        given(this.postsService.details(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{code}/details", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void content() {
        ContentVO contentVO = new ContentVO();
        contentVO.setContent("test");
        given(this.postsService.content(Mockito.anyString())).willReturn(Mono.just(contentVO));

        webTestClient.get().uri("/posts/{code}/content", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.content").isEqualTo("test");
    }

    @Test
    void content_error() {
        given(this.postsService.content(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/posts/{code}/content", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void like() {
        given(this.postsService.like(Mockito.anyString())).willReturn(Mono.just(2));

        webTestClient.patch().uri("/posts/{code}/like", "21213G0J2").exchange()
                .expectStatus().isOk();
        Mockito.verify(postsService, times(1)).like(Mockito.anyString());
    }

    @Test
    void like_error() {
        given(this.postsService.like(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.patch().uri("/posts/{code}/like", "21213G0J2").exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void previous() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.previous(Mockito.anyString())).willReturn(Mono.just(postsVO));

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
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.next(Mockito.anyString())).willReturn(Mono.just(postsVO));

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
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.search(Mockito.anyString())).willReturn(Flux.just(postsVO));

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
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.create(Mockito.any(PostsDTO.class))).willReturn(Mono.just(postsVO));

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        postsDTO.setContent("content");
        webTestClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.postsService.create(Mockito.any(PostsDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        postsDTO.setContent("content");
        webTestClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        // 构造返回对象
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostsDTO.class))).willReturn(Mono.just(postsVO));

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        postsDTO.setContent("content");
        webTestClient.put().uri("/posts/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange().expectStatus().isAccepted();
    }

    @Test
    void modify_error() {
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostsDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        postsDTO.setContent("content");
        webTestClient.put().uri("/posts/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange().expectStatus().isNotModified();
    }
}