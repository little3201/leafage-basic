/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.service.PortfolioService;
import io.leafage.basic.assets.vo.PortfolioVO;
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

/**
 * portfolio接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(PortfolioController.class)
class PortfolioControllerTest {

    @MockBean
    private PortfolioService portfolioService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        PortfolioVO portfolioVO = new PortfolioVO();
        portfolioVO.setTitle("test");
        given(this.portfolioService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(Flux.just(portfolioVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/portfolio").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(PortfolioVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.portfolioService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/portfolio").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("sort", "").build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        PortfolioVO portfolioVO = new PortfolioVO();
        portfolioVO.setTitle("test");
        given(this.portfolioService.fetch(Mockito.anyString())).willReturn(Mono.just(portfolioVO));

        webTestClient.get().uri("/portfolio/{code}", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.portfolioService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/portfolio/{code}", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void count() {
        given(this.portfolioService.count()).willReturn(Mono.just(2L));
        webTestClient.get().uri("/portfolio/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.portfolioService.count()).willThrow(new RuntimeException());
        webTestClient.get().uri("/portfolio/count").exchange().expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.portfolioService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/portfolio/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.portfolioService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/portfolio/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        // 构造返回对象
        PortfolioVO portfolioVO = new PortfolioVO();
        portfolioVO.setTitle("test");
        given(this.portfolioService.create(Mockito.any(PortfolioDTO.class))).willReturn(Mono.just(portfolioVO));

        // 构造请求对象
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setTitle("test");
        portfolioDTO.setUrl(Collections.singleton("../test.jpg"));
        webTestClient.post().uri("/portfolio").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolioDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.portfolioService.create(Mockito.any(PortfolioDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setTitle("test");
        portfolioDTO.setUrl(Collections.singleton("../test.jpg"));
        webTestClient.post().uri("/portfolio").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolioDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        // 构造返回对象
        PortfolioVO portfolioVO = new PortfolioVO();
        portfolioVO.setTitle("test");
        given(this.portfolioService.modify(Mockito.anyString(), Mockito.any(PortfolioDTO.class))).willReturn(Mono.just(portfolioVO));

        // 构造请求对象
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setTitle("test");
        portfolioDTO.setUrl(Collections.singleton("../test.jpg"));
        webTestClient.put().uri("/portfolio/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolioDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void modify_error() {
        given(this.portfolioService.modify(Mockito.anyString(), Mockito.any(PortfolioDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setTitle("test");
        portfolioDTO.setUrl(Collections.singleton("../test.jpg"));
        webTestClient.put().uri("/portfolio/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolioDTO).exchange()
                .expectStatus().isNotModified();
    }
}