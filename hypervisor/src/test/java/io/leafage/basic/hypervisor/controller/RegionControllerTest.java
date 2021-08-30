package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.BDDMockito.given;

/**
 * region接口测试
 *
 * @author liwenqiang 2021/8/30 9:38
 **/
@ExtendWith(SpringExtension.class)
@WebFluxTest(RegionController.class)
class RegionControllerTest {

    @MockBean
    private RegionService regionService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        RegionVO regionVO = new RegionVO();
        regionVO.setName("test");
        given(this.regionService.retrieve(0, 2)).willReturn(Flux.just(regionVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/region").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.regionService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/region").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        RegionVO regionVO = new RegionVO();
        regionVO.setName("test");
        given(this.regionService.fetch(Mockito.anyInt())).willReturn(Mono.just(regionVO));

        webTestClient.get().uri("/region/{code}", "1100").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.regionService.fetch(Mockito.anyInt())).willThrow(new RuntimeException());

        webTestClient.get().uri("/region/{code}", "1100").exchange().expectStatus().isNoContent();
    }

    @Test
    void count() {
        given(this.regionService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/region/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.regionService.count()).willThrow(new RuntimeException());

        webTestClient.get().uri("/region/count").exchange().expectStatus().isNoContent();
    }
}