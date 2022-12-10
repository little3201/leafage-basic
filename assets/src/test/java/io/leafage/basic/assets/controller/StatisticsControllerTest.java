package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * statistics controller test
 *
 * @author liwenqiang 2021/5/23 08:12
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(StatisticsController.class)
class StatisticsControllerTest {

    @MockBean
    private StatisticsService statisticsService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        StatisticsVO statisticsVO = new StatisticsVO();
        statisticsVO.setDate(LocalDate.now());
        statisticsVO.setOverDownloads(23);
        statisticsVO.setOverLikes(23.3);
        statisticsVO.setOverViewed(3.4);
        statisticsVO.setOverComments(2.23);
        Page<StatisticsVO> page = new PageImpl<>(List.of(statisticsVO));
        given(this.statisticsService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/statistics").queryParam("page", 0)
                        .queryParam("size", 7).build()).exchange().expectStatus().isOk()
                .expectBodyList(StatisticsVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.statisticsService.retrieve(Mockito.anyInt(), Mockito.anyInt()))
                .willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/statistics").queryParam("page", 0)
                .queryParam("size", 7).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        StatisticsVO totalVO = new StatisticsVO();
        totalVO.setViewed(121);
        given(this.statisticsService.fetch()).willReturn(Mono.just(totalVO));

        webTestClient.get().uri("/statistics/total").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.viewed").isEqualTo(121);
    }

    @Test
    void fetch_error() {
        given(this.statisticsService.fetch()).willThrow(new RuntimeException());

        webTestClient.get().uri("/statistics/total").exchange().expectStatus().isNoContent();
    }
}