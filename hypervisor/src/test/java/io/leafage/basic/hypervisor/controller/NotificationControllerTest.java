/*
 *  Copyright 2018-2022 the original author or authors.
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

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * notification controller test
 *
 * @author liwenqiang 2022/2/16 9:03
 **/
@ExtendWith(SpringExtension.class)
@WebFluxTest(NotificationController.class)
class NotificationControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("标题");
        notificationVO.setContent("内容");
        notificationVO.setReceiver("test");
        notificationVO.setModifyTime(LocalDateTime.now());
        Page<NotificationVO> voPage = new PageImpl<>(List.of(notificationVO));
        given(this.notificationService.retrieve(0, 2, false)).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/notifications").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("read", "false").build()).exchange()
                .expectStatus().isOk().expectBodyList(RoleVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.notificationService.retrieve(0, 2, false)).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/notifications").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("read", "false").build())
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("标题");
        given(this.notificationService.fetch(Mockito.anyString())).willReturn(Mono.just(notificationVO));

        webTestClient.get().uri("/notifications/{code}", "1100").exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.title").isEqualTo("标题");
    }

    @Test
    void fetch_error() {
        given(this.notificationService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/notifications/{code}", "1100").exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("标题");
        given(this.notificationService.create(Mockito.any(NotificationDTO.class))).willReturn(Mono.just(notificationVO));

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("标题");
        notificationDTO.setContent("内容信息");
        notificationDTO.setReceiver("test");
        webTestClient.post().uri("/notifications").bodyValue(notificationDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isEqualTo("标题");
    }

    @Test
    void create_error() {
        given(this.notificationService.create(Mockito.any(NotificationDTO.class))).willThrow(new RuntimeException());

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("标题");
        notificationDTO.setContent("内容信息");
        notificationDTO.setReceiver("test");
        webTestClient.post().uri("/notifications").bodyValue(notificationDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void remove() {
        given(this.notificationService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/notifications/{code}", "21612OL34").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.notificationService.remove(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/notifications/{code}", "21612OL34").exchange()
                .expectStatus().is4xxClientError();
    }
}