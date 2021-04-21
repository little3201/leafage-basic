/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.service.PostsService;
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
                .expectStatus().isOk();
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
        given(this.postsService.create(postsDTO)).willReturn(Mono.just(postsVO));
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
        given(this.postsService.modify("21213G0J2", postsDTO)).willReturn(Mono.just(postsVO));
        webClient.put().uri("/posts/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(postsDTO).exchange().expectStatus().isEqualTo(HttpStatus.ACCEPTED);
    }
}