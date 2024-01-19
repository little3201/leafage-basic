/*
 *  Copyright 2018-2024 the original author or authors.
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
import org.junit.jupiter.api.BeforeEach;
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

import java.time.Instant;

import static org.mockito.BDDMockito.given;

/**
 * comment controller test
 *
 * @author liwenqiang 2021-07-17 21:04
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(CommentController.class)
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private WebTestClient webTestClient;

    private CommentDTO commentDTO;
    private CommentVO commentVO;

    @BeforeEach
    void init() {
        commentDTO = new CommentDTO();
        commentDTO.setPostId(1L);
        commentDTO.setContext("test");

        commentVO = new CommentVO();
        commentVO.setPostId(commentDTO.getPostId());
        commentVO.setContext(commentDTO.getContext());
        commentVO.setCount(23L);
        commentVO.setLocation("中国上海");
        commentVO.setCountry("China");
        commentVO.setLastUpdatedAt(Instant.now());
    }

    @Test
    void comments() {
        given(this.commentService.comments(Mockito.anyLong())).willReturn(Flux.just(commentVO));

        webTestClient.get().uri("/comments/{id}", 1).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void comments_error() {
        given(this.commentService.comments(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/comments/{id}", 1).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void replies() {
        given(this.commentService.replies(Mockito.anyLong())).willReturn(Flux.just(commentVO));

        webTestClient.get().uri("/comments/{id}/replies", 1).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void repliers_error() {
        given(this.commentService.replies(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/comments/{id}/replies", 1).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.commentService.create(Mockito.any(CommentDTO.class))).willReturn(Mono.just(commentVO));

        webTestClient.post().uri("/comments").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.context").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.commentService.create(Mockito.any(CommentDTO.class))).willThrow(new RuntimeException());

        webTestClient.post().uri("/comments").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void remove() {
        given(this.commentService.remove(Mockito.anyLong())).willReturn(Mono.empty());
        webTestClient.delete().uri("/comments/{id}", 1).exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.commentService.remove(Mockito.anyLong())).willThrow(new RuntimeException());
        webTestClient.delete().uri("/comments/{id}", 1).exchange()
                .expectStatus().is4xxClientError();
    }
}