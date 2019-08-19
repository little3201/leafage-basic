/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.common.basic.AbstractController;

import javax.validation.constraints.NotNull;

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
     * 用户查询——分页
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity findUsers(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<ArticleInfo> articles = articleInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(articles.getContent())) {
            log.info("Not found anything about user with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(articles);
    }

    /**
     * 查询文章信息——根据ID
     *
     * @param articleId 文章ID
     * @return ResponseEntity
     */
    @GetMapping("/{articleId}")
    public ResponseEntity getArticle(@PathVariable String articleId) {
        ArticleInfo article = articleInfoService.getByArticleId(articleId);
        if (article == null) {
            log.info("Not found anything about article with articleId {}.", articleId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(article);
    }

}
