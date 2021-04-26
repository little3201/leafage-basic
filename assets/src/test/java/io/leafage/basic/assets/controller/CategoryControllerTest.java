/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void retrieve() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.fetch(Mockito.anyString())).willReturn(Mono.just(categoryVO));
        webClient.get().uri("/category").exchange().expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void retrieve_page() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.fetch(Mockito.anyString())).willReturn(Mono.just(categoryVO));
        webClient.get().uri(uriBuilder -> uriBuilder.path("/category").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void fetch() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.fetch(Mockito.anyString())).willReturn(Mono.just(categoryVO));
        webClient.get().uri("/category/{code}", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.alias").isEqualTo("test");
    }

    @Test
    void count() {
        given(this.categoryService.count()).willReturn(Mono.just(2L));
        webClient.get().uri("/category/count").exchange().expectStatus().isOk();
    }

    @Test
    void create() {
        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        // 构造返回对象
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willReturn(Mono.just(categoryVO));
        webClient.post().uri("/category").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody().jsonPath("$.alias").isNotEmpty();
    }

    @Test
    void modify() {
        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        // 构造返回对象
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.modify(Mockito.anyString(), Mockito.any(CategoryDTO.class))).willReturn(Mono.just(categoryVO));
        webClient.put().uri("/category/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isEqualTo(HttpStatus.ACCEPTED)
                .expectBody().jsonPath("$.alias").isNotEmpty();
    }
}