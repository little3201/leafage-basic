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

package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.BDDMockito.given;

/**
 * category controller test
 *
 * @author liwenqiang 2020-03-01 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private WebTestClient webTestClient;

    private CategoryDTO categoryDTO;
    private CategoryVO categoryVO;

    @BeforeEach
    void init() {
        // 构造请求对象
        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("test");
        categoryDTO.setDescription("描述信息");

        categoryVO = new CategoryVO();
        categoryVO.setCount(23L);
        categoryVO.setCategoryName(categoryDTO.getCategoryName());
        categoryVO.setModifyTime(LocalDateTime.now());
    }

    @Test
    void retrieve() {
        Page<CategoryVO> page = new PageImpl<>(List.of(categoryVO));
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/categories").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/categories").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        given(this.categoryService.fetch(Mockito.anyLong())).willReturn(Mono.just(categoryVO));

        webTestClient.get().uri("/categories/{id}", 1).exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.categoryName").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.categoryService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.get().uri("/categories/{id}", 1).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.categoryService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/categories/exist")
                        .queryParam("categoryName", "test").build()).exchange()
                .expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.categoryService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/categories/exist")
                .queryParam("categoryName", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willReturn(Mono.just(categoryVO));

        webTestClient.post().uri("/categories").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.categoryName").isEqualTo("test");
    }

    @Test
    void create_error() {
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());

        webTestClient.post().uri("/categories").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        given(this.categoryService.modify(Mockito.anyLong(), Mockito.any(CategoryDTO.class))).willReturn(Mono.just(categoryVO));

        webTestClient.put().uri("/categories/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.categoryName").isEqualTo("test");
    }

    @Test
    void modify_error() {
        given(this.categoryService.modify(Mockito.anyLong(), Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("test");
        webTestClient.put().uri("/categories/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.categoryService.remove(Mockito.anyLong())).willReturn(Mono.empty());

        webTestClient.delete().uri("/categories/{id}", 1).exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.categoryService.remove(Mockito.anyLong())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/categories/{id}", 1).exchange()
                .expectStatus().is4xxClientError();
    }
}