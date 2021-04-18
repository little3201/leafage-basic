/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.service.PostsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;

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
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(Mockito.any());
        webClient.get().uri("/posts").exchange().expectStatus().isOk();
        Mockito.verify(postsService, atLeastOnce()).create(Mockito.mock(PostsDTO.class));
    }

    @Test
    public void fetch() {
        given(this.postsService.fetch(Mockito.anyString())).willReturn(Mockito.any());
        webClient.get().uri("/posts/{code}", Mockito.anyString()).exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$:title").isNotEmpty();
    }

    @Test
    public void create() {
        webClient.post().uri("/posts").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Mockito.mock(PostsDTO.class)).exchange().expectStatus().isOk();
        Mockito.verify(postsService, atLeastOnce()).create(Mockito.mock(PostsDTO.class));
    }

    @Test
    public void modify() {
        webClient.put().uri("/posts/{code}", Mockito.anyString()).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Mockito.mock(PostsDTO.class)).exchange().expectStatus().isOk();
        Mockito.verify(postsService, atLeastOnce()).modify(Mockito.anyString(), Mockito.mock(PostsDTO.class));
    }
}