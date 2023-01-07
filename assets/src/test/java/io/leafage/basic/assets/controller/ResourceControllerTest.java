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

package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.mockito.BDDMockito.given;

/**
 * resource controller test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(ResourceController.class)
class ResourceControllerTest {

    @MockBean
    private ResourceService resourceService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        Page<ResourceVO> page = new PageImpl<>(List.of(resourceVO));
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyString())).willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resources").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(ResourceVO.class);
    }

    @Test
    void retrieve_category() {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        Page<ResourceVO> page = new PageImpl<>(List.of(resourceVO));
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyString())).willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resources").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2")
                        .queryParam("sort", "").build()).exchange()
                .expectStatus().isOk().expectBodyList(ResourceVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resources").queryParam("page", 0)
                        .queryParam("size", 2).queryParam("category", "21213G0J2")
                        .queryParam("sort", "").build()).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.fetch(Mockito.anyString())).willReturn(Mono.just(resourceVO));

        webTestClient.get().uri("/resources/{code}", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.title").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.resourceService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/resources/{code}", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.resourceService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resources/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.resourceService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/resources/exist")
                .queryParam("title", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        // 构造返回对象
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.create(Mockito.any(ResourceDTO.class))).willReturn(Mono.just(resourceVO));

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setCover("../test.jpg");
        resourceDTO.setTags(Set.of("sas"));

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21318000");
        categoryBO.setName("Technology");
        resourceDTO.setCategory(categoryBO);
        webTestClient.post().uri("/resources").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.resourceService.create(Mockito.any(ResourceDTO.class))).willThrow(new NoSuchElementException());

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setTags(Set.of("sas"));
        resourceDTO.setCover("../test.jpg");

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21318000");
        categoryBO.setName("Technology");
        resourceDTO.setCategory(categoryBO);
        webTestClient.post().uri("/resources").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        // 构造返回对象
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("test");
        given(this.resourceService.modify(Mockito.anyString(), Mockito.any(ResourceDTO.class))).willReturn(Mono.just(resourceVO));

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setTags(Set.of("sas"));
        resourceDTO.setCover("../test.jpg");

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21318000");
        categoryBO.setName("Technology");
        resourceDTO.setCategory(categoryBO);
        webTestClient.put().uri("/resources/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.title").isNotEmpty();
    }

    @Test
    void modify_error() {
        given(this.resourceService.modify(Mockito.anyString(), Mockito.any(ResourceDTO.class))).willThrow(new NoSuchElementException());

        // 构造请求对象
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setTags(Set.of("sas"));
        resourceDTO.setCover("../test.jpg");
        resourceDTO.setType('E');

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21318000");
        categoryBO.setName("Technology");
        resourceDTO.setCategory(categoryBO);
        webTestClient.put().uri("/resources/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resourceDTO).exchange()
                .expectStatus().isNotModified();
    }
}