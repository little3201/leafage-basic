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
    private WebTestClient webClient;

    @Test
    void retrieve() {
        PortfolioVO portfolioVO = new PortfolioVO();
        portfolioVO.setTitle("test");
        given(this.portfolioService.fetch(Mockito.anyString())).willReturn(Mono.just(portfolioVO));

        webClient.get().uri(uriBuilder -> uriBuilder.path("/portfolio").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(PortfolioVO.class);
    }

    @Test
    void retrieve_category() {
        PortfolioVO portfolioVO = new PortfolioVO();
        portfolioVO.setTitle("test");
        given(this.portfolioService.fetch(Mockito.anyString())).willReturn(Mono.just(portfolioVO));

        webClient.get().uri(uriBuilder -> uriBuilder.path("/portfolio").queryParam("page", 0)
                .queryParam("size", 2).queryParam("category", "21213G0J2").build()).exchange()
                .expectStatus().isOk().expectBodyList(PortfolioVO.class);
    }

    @Test
    void fetch() {
        PortfolioVO portfolioVO = new PortfolioVO();
        portfolioVO.setTitle("test");
        given(this.portfolioService.fetch(Mockito.anyString())).willReturn(Mono.just(portfolioVO));

        webClient.get().uri("/portfolio/{code}", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void count() {
        given(this.portfolioService.count()).willReturn(Mono.just(2L));
        webClient.get().uri("/portfolio/count").exchange().expectStatus().isOk();
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
        webClient.post().uri("/portfolio").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolioDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isNotEmpty();
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
        webClient.put().uri("/portfolio/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(portfolioDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }
}