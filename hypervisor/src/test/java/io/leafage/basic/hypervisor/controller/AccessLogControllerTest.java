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

import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;

/**
 * record controller test
 *
 * @author liwenqiang 2022/3/18 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(AccessLogController.class)
class AccessLogControllerTest {

    @MockBean
    private AccessLogService accessLogService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        AccessLogVO accessLogVO = new AccessLogVO();
        accessLogVO.setIp("12.1.2.1");
        accessLogVO.setLocation("某国某城市");
        accessLogVO.setDescription("更新个人资料");
        Page<AccessLogVO> page = new PageImpl<>(List.of(accessLogVO));
        given(this.accessLogService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/access-logs").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(AccessLogVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.accessLogService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new NoSuchElementException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/access-logs").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        // 构造返回对象
        AccessLogVO accessLogVO = new AccessLogVO();
        accessLogVO.setIp("12.1.2.1");
        accessLogVO.setLocation("某国某城市");
        accessLogVO.setDescription("更新个人资料");
        accessLogVO.setModifyTime(LocalDateTime.now());
        given(this.accessLogService.create(Mockito.any(AccessLogDTO.class))).willReturn(Mono.just(accessLogVO));

        // 构造请求对象
        AccessLogDTO accessLogDTO = new AccessLogDTO();
        accessLogDTO.setIp("12.1.2.1");
        accessLogDTO.setLocation("某国某城市");
        accessLogDTO.setDescription("更新个人资料");
        webTestClient.post().uri("/access-logs").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accessLogDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.location").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.accessLogService.create(Mockito.any(AccessLogDTO.class))).willThrow(new NoSuchElementException());

        // 构造请求对象
        AccessLogDTO accessLogDTO = new AccessLogDTO();
        accessLogDTO.setIp("12.1.2.1");
        accessLogDTO.setLocation("某国某城市");
        accessLogDTO.setDescription("更新个人资料");
        webTestClient.post().uri("/access-logs").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accessLogDTO).exchange()
                .expectStatus().is4xxClientError();
    }
}