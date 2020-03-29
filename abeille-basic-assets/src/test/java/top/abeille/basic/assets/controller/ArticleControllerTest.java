/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleControllerTest {

    private final WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:8764").build();

    @Before
    public void testBefore() {
        client.get().headers(h -> h.setBearerAuth("token"));
    }

    @Test
    public void fetchArticle() {
        ArticleDTO articleDTO = new ArticleDTO();
        client.post().uri("/article").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(articleDTO), ArticleDTO.class)
                .exchange().expectStatus().isOk()
                .expectBody().jsonPath("content").isNotEmpty();
    }
}