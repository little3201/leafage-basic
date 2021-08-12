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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

/**
 * posts接口测试类
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
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Flux.just(postsVO));

        webTestClient.get().uri("/posts").exchange().expectStatus().isOk().expectBodyList(PostsVO.class);
    }

    @Test
    void retrieve_page() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Flux.just(postsVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(PostsVO.class);
    }

    @Test
    void retrieve_page_category() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Flux.just(postsVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2").build()).exchange()
                .expectStatus().isOk().expectBodyList(PostsVO.class);
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
    void details() {
        PostsContentVO postsContentVO = new PostsContentVO();
        postsContentVO.setContent("test");
        given(this.postsService.details(Mockito.anyString())).willReturn(Mono.just(postsContentVO));

        webTestClient.get().uri("/posts/{code}/details", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.content").isEqualTo("test");
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
    void like() {
        given(this.postsService.like(Mockito.anyString())).willReturn(Mono.just(2));

        webTestClient.patch().uri("/posts/{code}/like", "21213G0J2").exchange()
                .expectStatus().isOk();
        Mockito.verify(postsService, times(1)).like(Mockito.anyString());
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
    void next() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.next(Mockito.anyString())).willReturn(Mono.just(postsVO));

        webTestClient.get().uri("/posts/{code}/next", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
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
    void count() {
        given(this.postsService.count()).willReturn(Mono.just(2L));
        webTestClient.get().uri("/posts/count").exchange().expectStatus().isOk();
    }

    @Test
    void exist() {
        given(this.postsService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/posts/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isOk();
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
        postsDTO.setSubtitle("create posts test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        postsDTO.setContent("content");
        webTestClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isNotEmpty();
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
        postsDTO.setSubtitle("create posts test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        postsDTO.setContent("content");
        webTestClient.put().uri("/posts/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange().expectStatus().isAccepted();
    }
}