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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(PostsController.class)
class PostsControllerTest {

    @MockBean
    private PostsService postsService;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void retrieve() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Flux.just(postsVO));
        webClient.get().uri("/posts").exchange()
                .expectStatus().isOk().expectBodyList(PostsVO.class);
    }

    @Test
    public void retrieve_page() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Flux.just(postsVO));
        webClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(PostsVO.class);
    }

    @Test
    public void retrieve_page_category() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Flux.just(postsVO));
        webClient.get().uri(uriBuilder -> uriBuilder.path("/posts").queryParam("page", 0)
                .queryParam("size", 2).queryParam("category", "21213G0J2").build()).exchange()
                .expectStatus().isOk().expectBodyList(PostsVO.class);
    }

    @Test
    public void fetch() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.fetch(Mockito.anyString())).willReturn(Mono.just(postsVO));
        webClient.get().uri("/posts/{code}", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    public void fetchDetails() {
        PostsContentVO postsContentVO = new PostsContentVO();
        postsContentVO.setContent("test");
        given(this.postsService.fetchDetails(Mockito.anyString())).willReturn(Mono.just(postsContentVO));
        webClient.get().uri("/posts/{code}/details", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.content").isEqualTo("test");
    }

    @Test
    public void fetchContent() {
        ContentVO contentVO = new ContentVO();
        contentVO.setContent("test");
        given(this.postsService.fetchContent(Mockito.anyString())).willReturn(Mono.just(contentVO));
        webClient.get().uri("/posts/{code}/content", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.content").isEqualTo("test");
    }

    @Test
    public void incrementLikes() {
        PostsVO postsVO = new PostsVO();
        postsVO.setViewed(2);
        given(this.postsService.incrementLikes(Mockito.anyString())).willReturn(Mono.just(postsVO));
        webClient.patch().uri("/posts/{code}/like", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.viewed").isEqualTo("2");
    }

    @Test
    public void previousPosts() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.previousPosts(Mockito.anyString())).willReturn(Mono.just(postsVO));
        webClient.get().uri("/posts/{code}/previous", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    public void nextPosts() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.nextPosts(Mockito.anyString())).willReturn(Mono.just(postsVO));
        webClient.get().uri("/posts/{code}/next", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    public void search() {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.search(Mockito.anyString())).willReturn(Flux.just(postsVO));
        webClient.get().uri(uriBuilder ->
                uriBuilder.path("/posts/search").queryParam("keyword", "test").build()).exchange()
                .expectStatus().isOk();
    }

    @Test
    public void count() {
        given(this.postsService.count()).willReturn(Mono.just(2L));
        webClient.get().uri("/posts/count").exchange().expectStatus().isOk();
    }

    @Test
    public void create() {
        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setSubtitle("create posts test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        // 构造返回对象
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.create(Mockito.any(PostsDTO.class))).willReturn(Mono.just(postsVO));
        webClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    public void modify() {
        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setSubtitle("create posts test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("21213G0J2");
        // 构造返回对象
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostsDTO.class))).willReturn(Mono.just(postsVO));
        webClient.put().uri("/posts/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange().expectStatus().isEqualTo(HttpStatus.ACCEPTED);
    }
}