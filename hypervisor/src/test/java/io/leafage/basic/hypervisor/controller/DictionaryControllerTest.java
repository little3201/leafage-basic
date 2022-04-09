package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
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
 * dictionary api test
 *
 * @author liwenqiang 2022/4/8 7:39
 **/
@ExtendWith(SpringExtension.class)
@WebFluxTest(DictionaryController.class)
class DictionaryControllerTest {

    @MockBean
    private DictionaryService dictionaryService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("test");
        dictionaryVO.setAlias("性别");
        dictionaryVO.setSuperior("2247K10L");
        dictionaryVO.setDescription("描述");
        given(this.dictionaryService.retrieve(0, 2)).willReturn(Flux.just(dictionaryVO));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/dictionary").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.dictionaryService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/dictionary").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("test");
        given(this.dictionaryService.fetch(Mockito.anyString())).willReturn(Mono.just(dictionaryVO));

        webTestClient.get().uri("/dictionary/{code}", "2247K100").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.dictionaryService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/dictionary/{code}", "2247K100").exchange().expectStatus().isNoContent();
    }

    @Test
    void lower() {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("test");
        given(this.dictionaryService.lower(Mockito.anyString())).willReturn(Flux.just(dictionaryVO));

        webTestClient.get().uri("/dictionary/{code}/lower", "2247K100").exchange()
                .expectStatus().isOk().expectBodyList(RegionVO.class);
    }

    @Test
    void lower_error() {
        given(this.dictionaryService.lower(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/dictionary/{code}/lower", "2247K100").exchange().expectStatus().isNoContent();
    }

    @Test
    void count() {
        given(this.dictionaryService.count()).willReturn(Mono.just(2L));

        webTestClient.get().uri("/dictionary/count").exchange().expectStatus().isOk();
    }

    @Test
    void count_error() {
        given(this.dictionaryService.count()).willThrow(new RuntimeException());

        webTestClient.get().uri("/dictionary/count").exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("Gender");
        dictionaryVO.setAlias("性别");
        dictionaryVO.setDescription("描述");
        given(this.dictionaryService.create(Mockito.any(DictionaryDTO.class))).willReturn(Mono.just(dictionaryVO));

        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("Gender");
        dictionaryDTO.setAlias("性别");
        dictionaryDTO.setDescription("描述");
        webTestClient.post().uri("/dictionary").bodyValue(dictionaryDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.name").isEqualTo("Gender");
    }

    @Test
    void create_error() {
        given(this.dictionaryService.create(Mockito.any(DictionaryDTO.class))).willThrow(new RuntimeException());

        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("Gender");
        dictionaryDTO.setAlias("性别");
        dictionaryDTO.setDescription("描述");
        webTestClient.post().uri("/dictionary").bodyValue(dictionaryDTO).exchange()
                .expectStatus().is4xxClientError();
    }
}