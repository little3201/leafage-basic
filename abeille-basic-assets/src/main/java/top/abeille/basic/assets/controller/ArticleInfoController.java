/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;
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
    public ResponseEntity fetchArticle() {
        Flux<ArticleInfo> infoFlux = articleInfoService.findAll();
        if (Objects.isNull(infoFlux)) {
            log.info("Not found anything about article.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(infoFlux);
    }

    /**
     * 查询文章信息——根据ID
     *
     * @param articleId 文章ID
     * @return ResponseEntity
     */
    @GetMapping("/{articleId}")
    public ResponseEntity getArticle(@PathVariable String articleId) {
        Mono<ArticleInfo> infoMono = articleInfoService.getByArticleId(articleId);
        if (Objects.isNull(infoMono)) {
            log.info("Not found anything about article with articleId: {}.", articleId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(infoMono);
    }

    /**
     * 保存文章信息
     *
     * @param articleInfo 文章
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity saveArticle(@RequestBody @Valid ArticleInfo articleInfo) {
        Mono<ArticleInfo> infoMono = articleInfoService.save(articleInfo);
        if (Objects.isNull(infoMono)) {
            log.error("Save user occurred error.");
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 保存文章信息
     *
     * @param articleInfo 文章
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity modifyArticle(@RequestBody @Valid ArticleInfo articleInfo) {
        if (Objects.isNull(articleInfo.getId())) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Mono<ArticleInfo> infoMono = articleInfoService.save(articleInfo);
        if (Objects.isNull(infoMono)) {
            log.error("Save user occurred error.");
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

}
