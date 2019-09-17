/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.common.basic.AbstractController;

import java.util.Objects;

/**
 * 文章信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/article")
public class ArticleInfoController extends AbstractController {

    private final ArticleInfoService articleInfoService;

    public ArticleInfoController(ArticleInfoService articleInfoService) {
        this.articleInfoService = articleInfoService;
    }

    /**
     * 文章查询——分页
     *
     * @return ResponseEntity
     */
    @GetMapping
    public Flux<ArticleInfo> fetchArticles() {
        return articleInfoService.findAll();
    }

    /**
     * 查询文章信息——根据ID
     *
     * @param articleId 文章ID
     * @return ResponseEntity
     */
    @GetMapping("/{articleId}")
    public ResponseEntity getArticle(@PathVariable String articleId) {
        Mono<ArticleInfo> articleInfo = articleInfoService.getByArticleId(articleId);
        if (Objects.isNull(articleInfo)) {
            log.info("Not found anything about article with articleId {}.", articleId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(articleInfo);
    }

}
