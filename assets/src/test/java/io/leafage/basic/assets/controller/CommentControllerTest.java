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
    private WebTestClient webClient;

    @Test
    void retrieve() {
        given(this.commentService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .willReturn(Flux.just(Mockito.mock(CommentVO.class)));

        webClient.get().uri(uriBuilder -> uriBuilder.path("/comment").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void count() {
        given(this.commentService.count()).willReturn(Mono.just(2L));
        webClient.get().uri("/comment/count").exchange().expectStatus().isOk();
    }

    @Test
    void create() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test");
        given(this.commentService.create(Mockito.any(CommentDTO.class))).willReturn(Mono.just(commentVO));

        // 构造请求对象
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("test");
        commentDTO.setEmail("test@test.com");
        commentDTO.setNickname("布吉岛");
        webClient.post().uri("/comment").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.content").isNotEmpty();
    }

    @Test
    void modify() {
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("test");
        given(this.commentService.modify(Mockito.anyString(), Mockito.any(CommentDTO.class))).willReturn(Mono.just(commentVO));

        // 构造请求对象
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("test");
        commentDTO.setEmail("test@test.com");
        commentDTO.setNickname("布吉岛");
        webClient.put().uri("/comment/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.content").isNotEmpty();
    }

    @Test
    void remove() {
        given(this.commentService.remove(Mockito.anyString())).willReturn(Mono.empty());
        webClient.delete().uri("/comment/{code}", "21213G0J2").exchange()
                .expectStatus().isOk();
    }
}