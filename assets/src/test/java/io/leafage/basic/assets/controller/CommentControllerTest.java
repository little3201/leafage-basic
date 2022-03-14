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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

/**
 * comment 接口测试类
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
        given(this.commentService.retrieve(Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Flux.just(commentVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/comment").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.commentService.retrieve(Mockito.anyInt(), Mockito.anyInt()))
                .willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/comment").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void relation() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test content");
        given(this.commentService.relation(Mockito.anyString())).willReturn(Flux.just(commentVO));

        webTestClient.get().uri("/comment/{code}", "21319JO01").exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void relation_error() {
        given(this.commentService.relation(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/comment/{code}", "21319JO01").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void replies() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test content");
        given(this.commentService.replies(Mockito.anyString())).willReturn(Flux.just(commentVO));

        webTestClient.get().uri("/comment/{code}/replies", "21319JO01").exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void repliers_error() {
        given(this.commentService.replies(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/comment/{code}/replies", "21319JO01").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void count() {
        given(this.commentService.count()).willReturn(Mono.just(2L));
        webTestClient.get().uri("/comment/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.commentService.count()).willThrow(new RuntimeException());
        webTestClient.get().uri("/comment/count").exchange().expectStatus().isNoContent();
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
        webTestClient.post().uri("/comment").contentType(MediaType.APPLICATION_JSON)
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
        webTestClient.post().uri("/comment").contentType(MediaType.APPLICATION_JSON)
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
        webTestClient.put().uri("/comment/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
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
        webTestClient.put().uri("/comment/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.commentService.remove(Mockito.anyString())).willReturn(Mono.empty());
        webTestClient.delete().uri("/comment/{code}", "21213G0J2").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.commentService.remove(Mockito.anyString())).willThrow(new RuntimeException());
        webTestClient.delete().uri("/comment/{code}", "21213G0J2").exchange()
                .expectStatus().is4xxClientError();
    }
}