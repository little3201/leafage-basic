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

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CategoryVO;
import io.leafage.basic.assets.vo.CommentVO;
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

import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * comment controller test
 *
 * @author liwenqiang 2021/7/17 21:04
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(CommentController.class)
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test content");
        Page<CommentVO> page = new PageImpl<>(List.of(commentVO));
        given(this.commentService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/comments").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.commentService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/comments").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void relation() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test content");
        given(this.commentService.relation(Mockito.anyString())).willReturn(Flux.just(commentVO));

        webTestClient.get().uri("/comments/{code}", "21319JO01").exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void relation_error() {
        given(this.commentService.relation(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/comments/{code}", "21319JO01").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void replies() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test content");
        given(this.commentService.replies(Mockito.anyString())).willReturn(Flux.just(commentVO));

        webTestClient.get().uri("/comments/{code}/replies", "21319JO01").exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void repliers_error() {
        given(this.commentService.replies(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/comments/{code}/replies", "21319JO01").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void create() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test");
        given(this.commentService.create(Mockito.any(CommentDTO.class))).willReturn(Mono.just(commentVO));

        // 构造请求对象
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPosts("21213G0J2");
        commentDTO.setContent("test");
        webTestClient.post().uri("/comments").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.content").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.commentService.create(Mockito.any(CommentDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPosts("21213G0J2");
        commentDTO.setContent("test");
        webTestClient.post().uri("/comments").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test");
        given(this.commentService.modify(Mockito.anyString(), Mockito.any(CommentDTO.class))).willReturn(Mono.just(commentVO));

        // 构造请求对象
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPosts("21213G0J2");
        commentDTO.setContent("test");
        webTestClient.put().uri("/comments/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.content").isNotEmpty();
    }

    @Test
    void modify_error() {
        given(this.commentService.modify(Mockito.anyString(), Mockito.any(CommentDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPosts("21213G0J2");
        commentDTO.setContent("test");
        webTestClient.put().uri("/comments/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.commentService.remove(Mockito.anyString())).willReturn(Mono.empty());
        webTestClient.delete().uri("/comments/{code}", "21213G0J2").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.commentService.remove(Mockito.anyString())).willThrow(new RuntimeException());
        webTestClient.delete().uri("/comments/{code}", "21213G0J2").exchange()
                .expectStatus().is4xxClientError();
    }
}