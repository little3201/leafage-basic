/*
 *  Copyright 2018-2024 the original author or authors.
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

import io.leafage.basic.hypervisor.dto.MessageDTO;
import io.leafage.basic.hypervisor.service.MessageService;
import io.leafage.basic.hypervisor.vo.MessageVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * message controller test
 *
 * @author liwenqiang 2022-02-16 9:03
 **/
@ExtendWith(SpringExtension.class)
@WebFluxTest(MessageController.class)
class MessageControllerTest {

    @MockBean
    private MessageService messageService;

    @Autowired
    private WebTestClient webTestClient;

    private MessageVO messageVO;
    private MessageDTO messageDTO;

    @BeforeEach
    void init() {
        messageVO = new MessageVO();
        messageVO.setTitle("标题");
        messageVO.setContext("内容");
        messageVO.setReceiver("test");
        messageVO.setLastUpdatedTime(LocalDateTime.now());

        messageDTO = new MessageDTO();
        messageDTO.setTitle("标题");
        messageDTO.setContext("内容信息");
        messageDTO.setReceiver("test");
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<MessageVO> voPage = new PageImpl<>(List.of(messageVO), pageable, 1L);
        given(this.messageService.retrieve(0, 2, "test")).willReturn(Mono.just(voPage));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/messages").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("receiver", "test").build()).exchange()
                .expectStatus().isOk().expectBodyList(MessageVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.messageService.retrieve(0, 2, "test")).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/messages").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("receiver", "test").build())
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.messageService.fetch(Mockito.anyLong())).willReturn(Mono.just(messageVO));

        webTestClient.get().uri("/messages/{id}", 1L).exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("标题");
    }

    @Test
    void fetch_error() {
        given(this.messageService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/messages/{id}", 1L).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.messageService.create(Mockito.any(MessageDTO.class))).willReturn(Mono.just(messageVO));

        webTestClient.post().uri("/messages").bodyValue(messageDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isEqualTo("标题");
    }

    @Test
    void create_error() {
        given(this.messageService.create(Mockito.any(MessageDTO.class))).willThrow(new RuntimeException());

        webTestClient.post().uri("/messages").bodyValue(messageDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void remove() {
        given(this.messageService.remove(Mockito.anyLong())).willReturn(Mono.empty());

        webTestClient.delete().uri("/messages/{id}", 1L).exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.messageService.remove(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/messages/{id}", 1L).exchange()
                .expectStatus().is4xxClientError();
    }
}