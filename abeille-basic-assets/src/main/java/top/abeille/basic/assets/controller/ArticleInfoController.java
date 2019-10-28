/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
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
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity fetchArticle(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<ArticleVO> articles = articleInfoService.fetchByPage(pageable);
        if (CollectionUtils.isEmpty(articles.getContent())) {
            logger.info("Not found anything about user with pageable.");
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
    public ResponseEntity queryArticle(@PathVariable Long articleId) {
        ArticleVO article = articleInfoService.queryById(articleId);
        if (article == null) {
            logger.info("Not found anything about article with articleId {}.", articleId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(article);
    }

    /**
     * 保存文章信息
     *
     * @param articleDTO 文章内容
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity saveArticle(@RequestBody @Valid ArticleDTO articleDTO) {
        ArticleVO articleVO;
        try {
            articleVO = articleInfoService.create(articleDTO);
        } catch (Exception e) {
            logger.error("Save article occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(articleVO);
    }
}
