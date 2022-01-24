/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
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

import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;

/**
 * portfolio接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(ResourceController.class)
class ResourceControllerTest {

    @MockBean
    private ResourceService resourceService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve_page() {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(Flux.just(resourceVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resource").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(ResourceVO.class);
    }

    @Test
    void retrieve_page_category() {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).willReturn(Flux.just(resourceVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resource").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2").build()).exchange()
                .expectStatus().isOk().expectBodyList(ResourceVO.class);
    }

    @Test
    void retrieve_page_error() {
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resource").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("sort", "").build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.fetch(Mockito.anyString())).willReturn(Mono.just(resourceVO));

        webTestClient.get().uri("/resource/{code}", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.resourceService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/resource/{code}", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void count() {
        given(this.resourceService.count(Mockito.anyString())).willReturn(Mono.just(2L));
        webTestClient.get().uri("/resource/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.resourceService.count(Mockito.anyString())).willThrow(new RuntimeException());
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resource/count")
                .queryParam("category", "").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.resourceService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resource/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.resourceService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resource/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        // 构造返回对象
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.create(Mockito.any(ResourceDTO.class))).willReturn(Mono.just(resourceVO));

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setCover("../test.jpg");
        resourceDTO.setCategory("21318000");
        webTestClient.post().uri("/resource").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.resourceService.create(Mockito.any(ResourceDTO.class))).willThrow(new NoSuchElementException());

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setCover("../test.jpg");
        resourceDTO.setCategory("21318000");
        webTestClient.post().uri("/resource").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        // 构造返回对象
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.modify(Mockito.anyString(), Mockito.any(ResourceDTO.class))).willReturn(Mono.just(resourceVO));

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setCover("../test.jpg");
        resourceDTO.setCategory("21318000");
        webTestClient.put().uri("/resource/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void modify_error() {
        given(this.resourceService.modify(Mockito.anyString(), Mockito.any(ResourceDTO.class))).willThrow(new NoSuchElementException());

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setCover("../test.jpg");
        resourceDTO.setType('E');
        resourceDTO.setCategory("21318000");
        webTestClient.put().uri("/resource/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().isNotModified();
    }
}