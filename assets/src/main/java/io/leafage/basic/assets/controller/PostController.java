/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * posts controller.
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/posts")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostsService postsService;

    public PostController(PostsService postsService) {
        this.postsService = postsService;
    }

    /**
     * retrieve with page .
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 分页结果集
     */
    @GetMapping
    public ResponseEntity<Page<PostVO>> retrieve(@RequestParam int page, @RequestParam int size, String sort) {
        Page<PostVO> voPage;
        try {
            voPage = postsService.retrieve(page, size, sort);
        } catch (Exception e) {
            logger.error("Retrieve posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * fetch with id .
     *
     * @param id 主键
     * @return 帖子信息，不包括内容
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostVO> fetch(@PathVariable Long id) {
        PostVO vo;
        try {
            vo = postsService.fetch(id);
        } catch (Exception e) {
            logger.error("Fetch posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * previous.
     *
     * @param id 主键
     * @return 帖子信息，不包括内容
     */
    @GetMapping("/{id}/previous")
    public ResponseEntity<PostVO> previous(@PathVariable Long id) {
        PostVO vo;
        try {
            vo = postsService.previous(id);
        } catch (Exception e) {
            logger.error("Fetch posts previous occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * next.
     *
     * @param id 主键
     * @return 帖子信息，不包括内容
     */
    @GetMapping("/{id}/next")
    public ResponseEntity<PostVO> next(@PathVariable Long id) {
        PostVO vo;
        try {
            vo = postsService.next(id);
        } catch (Exception e) {
            logger.error("Fetch posts next occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * 查询帖子详细信息，同时viewed自增1
     *
     * @param id 主键
     * @return 帖子所有信息，包括内容
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<PostVO> details(@PathVariable Long id) {
        PostVO vo;
        try {
            vo = postsService.details(id);
        } catch (Exception e) {
            logger.info("Fetch posts details occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vo);
    }

    /**
     * 查询帖子是否存在
     *
     * @param title 标题
     * @return 帖子是否已存在
     */
    @GetMapping("/exist")
    public ResponseEntity<Boolean> exist(@RequestParam String title) {
        boolean exist;
        try {
            exist = postsService.exist(title);
        } catch (Exception e) {
            logger.info("Fetch posts exist occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(exist);
    }

    /**
     * 保存文章信息
     *
     * @param postDTO 文章内容
     * @return 帖子信息
     */
    @PostMapping
    public ResponseEntity<PostVO> create(@RequestBody @Valid PostDTO postDTO) {
        PostVO vo;
        try {
            vo = postsService.create(postDTO);
        } catch (Exception e) {
            logger.error("Save posts occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * 修改帖子信息
     *
     * @param id      主键
     * @param postDTO 帖子信息
     * @return 修改后的帖子信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostVO> modify(@PathVariable Long id, @RequestBody @Valid PostDTO postDTO) {
        PostVO vo;
        try {
            vo = postsService.modify(id, postDTO);
        } catch (Exception e) {
            logger.error("Modify posts occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * 删除帖子信息
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        try {
            postsService.remove(id);
        } catch (Exception e) {
            logger.error("Remove posts occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}
