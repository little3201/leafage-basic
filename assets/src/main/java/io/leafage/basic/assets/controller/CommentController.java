/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * comment controller
 *
 * @author liwenqiang 2021/7/17 21:01
 **/
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 分页查询
     *
     * @param page 分页位置
     * @param size 分页大小
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Flux<CommentVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Flux<CommentVO> voFlux;
        try {
            voFlux = commentService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve comment occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 统计记录数
     *
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = commentService.count();
        } catch (Exception e) {
            logger.error("Count category occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 根据 code 查询信息
     *
     * @param code 帖子代码
     * @return 关联的评论
     */
    @GetMapping("/{code}")
    public ResponseEntity<Flux<CommentVO>> relation(@PathVariable String code) {
        Flux<CommentVO> voFlux;
        try {
            voFlux = commentService.relation(code);
        } catch (Exception e) {
            logger.error("Fetch comment by posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }


    /**
     * 根据 code 查询回复信息
     *
     * @param code 评论代码
     * @return 关联的评论
     */
    @GetMapping("/{code}/replies")
    public ResponseEntity<Flux<CommentVO>> replies(@PathVariable String code) {
        Flux<CommentVO> voFlux;
        try {
            voFlux = commentService.replies(code);
        } catch (Exception e) {
            logger.error("Retrieve comment repliers occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 添加信息
     *
     * @param commentDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<CommentVO>> create(@RequestBody @Valid CommentDTO commentDTO) {
        Mono<CommentVO> voMono;
        try {
            voMono = commentService.create(commentDTO);
        } catch (Exception e) {
            logger.error("Create comment occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改信息
     *
     * @param code       代码
     * @param commentDTO 要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<CommentVO>> modify(@PathVariable String code, @RequestBody @Valid CommentDTO commentDTO) {
        Mono<CommentVO> voMono;
        try {
            voMono = commentService.modify(code, commentDTO);
        } catch (Exception e) {
            logger.error("Modify comment occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 删除信息
     *
     * @param code 代码
     * @return 200状态码，异常时返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Mono<Void>> remove(@PathVariable String code) {
        Mono<Void> voidMono;
        try {
            voidMono = commentService.remove(code);
        } catch (Exception e) {
            logger.error("Remove comment occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(voidMono);
    }

}
