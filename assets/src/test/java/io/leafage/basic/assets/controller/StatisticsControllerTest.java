package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

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
        statisticsVO.setLikes(233);
        statisticsVO.setViewed(34);
        statisticsVO.setComments(23);
        given(this.statisticsService.retrieve()).willReturn(Flux.just(statisticsVO));

        webTestClient.get().uri("/statistics").exchange().expectStatus().isOk()
                .expectBodyList(StatisticsVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.statisticsService.retrieve()).willThrow(new RuntimeException());

        webTestClient.get().uri("/statistics").exchange().expectStatus().isNoContent();
    }

}