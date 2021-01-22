/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import top.abeille.basic.assets.document.Posts;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.service.PostsService;

import static org.mockito.Mockito.atLeastOnce;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(PostsService.class)
public class PostsControllerTest {

    @MockBean
    private PostsRepository postsRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void retrieve() {
        Mockito.when(postsRepository.save(Mockito.mock(Posts.class))).thenReturn(Mockito.any());
        webClient.post().uri("/article").contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Mockito.mock(Posts.class))).exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("content").isNotEmpty();
        Mockito.verify(postsRepository, atLeastOnce()).save(Mockito.mock(Posts.class));
    }

    @Test
    public void fetch() {
        webClient.get().uri("/article/{businessId}", "AT7124EVA").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("content").isNotEmpty();
    }
}