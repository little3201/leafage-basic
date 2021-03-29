/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 文章信息接口
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/posts")
public class PostsController {

    private final Logger logger = LoggerFactory.getLogger(PostsController.class);

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
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
    public ResponseEntity<Object> retrieve(@RequestParam int page, @RequestParam int size, String order) {
        Page<PostsVO> voPage = postsService.retrieve(page, size, order);
        if (voPage.hasContent()) {
            return ResponseEntity.ok(voPage);
        }
        logger.info("Not found anything about posts with pageable.");
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
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
