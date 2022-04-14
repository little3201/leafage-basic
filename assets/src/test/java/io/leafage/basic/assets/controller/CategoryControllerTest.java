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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;
import static org.mockito.BDDMockito.given;

/**
 * category controller test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void retrieve() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        Page<CategoryVO> page = new PageImpl<>(List.of(categoryVO));
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(Mono.just(page));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/category").queryParam("page", 0)
                        .queryParam("size", 2).build()).exchange()
                .expectStatus().isOk().expectBodyList(CategoryVO.class);
    }

    @Test
    void retrieve_error() {
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/category").queryParam("page", 0)
                .queryParam("size", 2).build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void fetch() {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.fetch(Mockito.anyString())).willReturn(Mono.just(categoryVO));

        webTestClient.get().uri("/category/{code}", "21213G0J2").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.alias").isEqualTo("test");
    }

    @Test
    void fetch_error() {
        given(this.categoryService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri("/category/{code}", "21213G0J2").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void exist() {
        given(this.categoryService.exist(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/category/exist")
                .queryParam("alias", "test").build()).exchange().expectStatus().isOk();
    }

    @Test
    void exist_error() {
        given(this.categoryService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/category/exist")
                .queryParam("alias", "test").build()).exchange().expectStatus().isNoContent();
    }

    @Test
    void create() {
        // 构造返回对象
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willReturn(Mono.just(categoryVO));

        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        webTestClient.post().uri("/category").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.alias").isNotEmpty();
    }

    @Test
    void create_error() {
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        webTestClient.post().uri("/category").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void modify() {
        // 构造返回对象
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setAlias("test");
        given(this.categoryService.modify(Mockito.anyString(), Mockito.any(CategoryDTO.class))).willReturn(Mono.just(categoryVO));

        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        webTestClient.put().uri("/category/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isAccepted()
                .expectBody().jsonPath("$.alias").isNotEmpty();
    }

    @Test
    void modify_error() {
        given(this.categoryService.modify(Mockito.anyString(), Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        webTestClient.put().uri("/category/{code}", "21213G0J2").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryDTO).exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void remove() {
        given(this.categoryService.remove(Mockito.anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/category/{code}", "21213G0J2").exchange()
                .expectStatus().isOk();
    }

    @Test
    void remove_error() {
        given(this.categoryService.remove(Mockito.anyString())).willThrow(new RuntimeException());

        webTestClient.delete().uri("/category/{code}", "21213G0J2").exchange()
                .expectStatus().is4xxClientError();
    }
}