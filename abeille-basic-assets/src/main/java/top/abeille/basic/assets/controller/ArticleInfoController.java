/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

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
    public Flux<ArticleVO> retrieveArticle() {
        Sort sort = super.initSortProperties();
        return articleInfoService.retrieveAll(sort);
    }

    /**
     * 查询文章信息——根据ID
     *
     * @param articleId 文章ID
     * @return ResponseEntity
     */
    @GetMapping("/{articleId}")
    public Mono<ResponseEntity<ArticleVO>> fetchArticle(@PathVariable Long articleId) {
        return articleInfoService.fetchById(articleId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 添加文章信息
     *
     * @param articleDTO 文章
     * @return ResponseEntity
     */
    @PostMapping
    public Mono<ResponseEntity<ArticleVO>> createArticle(@RequestBody @Valid ArticleDTO articleDTO) {
        return articleInfoService.create(articleDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 修改文章信息
     *
     * @param articleDTO 文章
     * @return ResponseEntity
     */
    @PutMapping("/{articleId}")
    public Mono<ResponseEntity<ArticleVO>> modifyArticle(@PathVariable Long articleId, @RequestBody @Valid ArticleDTO articleDTO) {
        return articleInfoService.modify(articleId, articleDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

}
