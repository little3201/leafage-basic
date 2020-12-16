/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.service.PostsService;
import top.abeille.basic.assets.vo.PostsVO;

import javax.validation.Valid;

/**
 * 文章信息接口
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/posts")
public class ArticleInfoController {

    private final Logger logger = LoggerFactory.getLogger(ArticleInfoController.class);

    private final PostsService postsService;

    public ArticleInfoController(PostsService postsService) {
        this.postsService = postsService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<Object> retrieve(int page, int size) {
        Page<PostsVO> articles = postsService.retrieves(page, size);
        if (CollectionUtils.isEmpty(articles.getContent())) {
            logger.info("Not found anything about user with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(articles);
    }

    /**
     * 查询帖子
     *
     * @param code 代码
     * @return ResponseEntity
     */
    @GetMapping("/{code}")
    public ResponseEntity<Object> fetch(@PathVariable String code) {
        PostsVO article = postsService.fetch(code);
        if (article == null) {
            logger.info("Not found anything about article with code {}.", code);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(article);
    }

    /**
     * 保存文章信息
     *
     * @param postsDTO 文章内容
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid PostsDTO postsDTO) {
        PostsVO postsVO;
        try {
            postsVO = postsService.create(postsDTO);
        } catch (Exception e) {
            logger.error("Save article occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(postsVO);
    }
}
