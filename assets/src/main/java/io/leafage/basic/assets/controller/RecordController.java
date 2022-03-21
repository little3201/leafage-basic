/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.RecordDTO;
import io.leafage.basic.assets.service.RecordService;
import io.leafage.basic.assets.vo.RecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * record controller
 *
 * @author liwenqiang 2022-03-18 21:01
 **/
@RestController
@RequestMapping("/record")
public class RecordController {

    private final Logger logger = LoggerFactory.getLogger(RecordController.class);

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * 查询
     *
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Flux<RecordVO>> retrieve() {
        Flux<RecordVO> voFlux;
        try {
            voFlux = recordService.retrieve();
        } catch (Exception e) {
            logger.error("Retrieve record occurred an error: ", e);
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
            count = recordService.count();
        } catch (Exception e) {
            logger.error("Count record occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 新增信息
     *
     * @param recordDTO 帖子代码
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<RecordVO>> create(@RequestBody RecordDTO recordDTO) {
        Mono<RecordVO> voMono;
        try {
            voMono = recordService.create(recordDTO);
        } catch (Exception e) {
            logger.error("Create record occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

}
