/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.service.DetailsService;
import top.abeille.basic.assets.service.impl.PostsServiceImpl;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@SpringBootTest
public class PostsControllerTest {

    @MockBean
    private PostsRepository postsRepository;

    @MockBean
    private DetailsService detailsService;


    private final WebTestClient client = WebTestClient.bindToController(new PostsController(
            new PostsServiceImpl(postsRepository, detailsService))).build();

    @Test
    public void retrieveArticle() {
        PostsDTO postsDTO = new PostsDTO();
        client.post().uri("/article").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(postsDTO), PostsDTO.class).exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("content").isNotEmpty();
    }

    @Test
    public void fetchArticle() {
        client.get().uri("/article/{businessId}", "AT7124EVA").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("content").isNotEmpty();
    }
}