/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostBO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * posts controller
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
     * @param page     页码
     * @param size     大小
     * @param sort     排序字段
     * @param category 分类
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Mono<Page<PostVO>>> retrieve(@RequestParam int page, @RequestParam int size,
                                                       String sort, String category) {
        Mono<Page<PostVO>> pageMono;
        try {
            pageMono = postsService.retrieve(page, size, sort, category);
        } catch (Exception e) {
            logger.error("Retrieve posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pageMono);
    }

    /**
     * title 关键字查询
     *
     * @param keyword 关键字
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping("/search")
    public ResponseEntity<Flux<PostVO>> search(@RequestParam String keyword) {
        Flux<PostVO> voFlux;
        try {
            voFlux = postsService.search(keyword);
        } catch (Exception e) {
            logger.error("Search posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据 code 查询，包含内容
     *
     * @param code 代码
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<PostVO>> fetch(@PathVariable String code) {
        Mono<PostVO> voMono;
        try {
            voMono = postsService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch posts details occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 是否已存在
     *
     * @param title 标题
     * @return true-是，false-否
     */
    @GetMapping("/exist")
    public ResponseEntity<Mono<Boolean>> exist(@RequestParam String title) {
        Mono<Boolean> existsMono;
        try {
            existsMono = postsService.exist(title);
        } catch (Exception e) {
            logger.error("Check posts is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(existsMono);
    }

    /**
     * 查询下一条记录
     *
     * @param code 代码
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/{code}/next")
    public ResponseEntity<Mono<PostVO>> next(@PathVariable String code) {
        Mono<PostVO> voMono;
        try {
            voMono = postsService.next(code);
        } catch (Exception e) {
            logger.error("Fetch next posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 查询上一条记录
     *
     * @param code 代码
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/{code}/previous")
    public ResponseEntity<Mono<PostVO>> previous(@PathVariable String code) {
        Mono<PostVO> voMono;
        try {
            voMono = postsService.previous(code);
        } catch (Exception e) {
            logger.error("Fetch previous posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 添加信息
     *
     * @param postDTO 要添加的数据
     * @return 添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<PostVO>> create(@RequestBody @Valid PostBO postDTO) {
        Mono<PostVO> voMono;
        try {
            voMono = postsService.create(postDTO);
        } catch (Exception e) {
            logger.error("Create posts occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改信息
     *
     * @param code    代码
     * @param postDTO 要修改的数据
     * @return 修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<PostVO>> modify(@PathVariable String code, @RequestBody @Valid PostBO postDTO) {
        Mono<PostVO> voMono;
        try {
            voMono = postsService.modify(code, postDTO);
        } catch (Exception e) {
            logger.error("Modify posts occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

}
