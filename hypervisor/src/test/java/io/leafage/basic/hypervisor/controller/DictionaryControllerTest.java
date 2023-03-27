/*
 *  Copyright 2018-2023 the original author or authors.
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

package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import io.leafage.basic.hypervisor.vo.RegionVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.BeforeEach;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    private DictionaryDTO dictionaryDTO;
    private DictionaryVO dictionaryVO;

    @BeforeEach
    void init() {
        dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setDictionaryName("Gender");
        dictionaryDTO.setDescription("描述");

        dictionaryVO = new DictionaryVO();
        dictionaryVO.setDictionaryName("test");
        dictionaryVO.setDescription("性别-男");
    }

    @Test
    void retrieve() {
        Page<DictionaryVO> voPage = new PageImpl<>(List.of(dictionaryVO));
        given(this.dictionaryService.retrieve(0, 2)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/dictionaries").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.dictionaryService.retrieve(0, 2)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/dictionaries").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.dictionaryService.fetch(Mockito.anyLong())).willReturn(Mono.just(dictionaryVO));

        webTestClient.get().uri("/dictionaries/{id}", 1L).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.dictionaryName").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.dictionaryService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/dictionaries/{id}", 1L).exchange().expectStatus().isNoContent();
    }

    @Test
    void superior() {
        given(this.dictionaryService.superior()).willReturn(Flux.just(dictionaryVO));

        webTestClient.get().uri("/dictionaries/superior").exchange()
                .expectStatus().isOk().expectBodyList(RegionVO.class);
    }

    @Test
    void superior_error() {
        given(this.dictionaryService.superior()).willThrow(new RuntimeException());

        webTestClient.get().uri("/dictionaries/superior").exchange().expectStatus().isNoContent();
    }

    @Test
    void lower() {
        given(this.dictionaryService.subordinates(Mockito.anyLong())).willReturn(Flux.just(dictionaryVO));

        webTestClient.get().uri("/dictionaries/{id}/subordinates", 1L).exchange()
                .expectStatus().isOk().expectBodyList(RegionVO.class);
    }

    @Test
    void lower_error() {
        given(this.dictionaryService.subordinates(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/dictionaries/{id}/subordinates", 1L).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.dictionaryService.create(Mockito.any(DictionaryDTO.class))).willReturn(Mono.just(dictionaryVO));

        webTestClient.post().uri("/dictionaries").bodyValue(dictionaryDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.dictionaryName").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.dictionaryService.create(Mockito.any(DictionaryDTO.class))).willThrow(new RuntimeException());

        webTestClient.post().uri("/dictionaries").bodyValue(dictionaryDTO).exchange()
                .expectStatus().is4xxClientError();
    }
}