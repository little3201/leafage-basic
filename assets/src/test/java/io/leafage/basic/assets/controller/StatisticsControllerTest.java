package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import static org.mockito.BDDMockito.given;

/**
 * 统计接口测试类
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
        given(this.statisticsService.retrieve(Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Flux.just(statisticsVO));

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

}