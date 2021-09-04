/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

/**
 * resource controller
 *
 * @author liwenqiang 2020/2/20 9:54
 **/
@RestController
@RequestMapping("/resource")
public class ResourceController {

    private final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Flux<ResourceVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                     String sort) {
        Flux<ResourceVO> voFlux;
        try {
            voFlux = resourceService.retrieve(page, size, sort);
        } catch (Exception e) {
            logger.error("Retrieve portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<ResourceVO>> fetch(@PathVariable String code) {
        Mono<ResourceVO> voMono;
        try {
            voMono = resourceService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
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
            count = resourceService.count();
        } catch (Exception e) {
            logger.error("Count portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
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
            existsMono = resourceService.exist(title);
        } catch (Exception e) {
            logger.error("Check portfolio is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(existsMono);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param resourceDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<ResourceVO>> create(@RequestBody @Valid ResourceDTO resourceDTO) {
        Mono<ResourceVO> voMono;
        try {
            voMono = resourceService.create(resourceDTO);
        } catch (Exception e) {
            logger.error("Create portfolio occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code        代码
     * @param resourceDTO 要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<ResourceVO>> modify(@PathVariable String code, @RequestBody @Valid ResourceDTO resourceDTO) {
        Mono<ResourceVO> voMono;
        try {
            voMono = resourceService.modify(code, resourceDTO);
        } catch (Exception e) {
            logger.error("Modify portfolio occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

}
