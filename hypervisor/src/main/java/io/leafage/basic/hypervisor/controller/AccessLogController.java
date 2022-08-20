/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * record controller
 *
 * @author liwenqiang 2022-03-18 21:01
 **/
@RestController
@RequestMapping("/access-logs")
public class AccessLogController {

    private final Logger logger = LoggerFactory.getLogger(AccessLogController.class);

    private final AccessLogService accessLogService;

    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    /**
     * 查询
     *
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Mono<Page<AccessLogVO>>> retrieve(@RequestParam int page, @RequestParam int size) {
        Mono<Page<AccessLogVO>> pageMono;
        try {
            pageMono = accessLogService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve record occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pageMono);
    }

    /**
     * 新增信息
     *
     * @param accessLogDTO 帖子代码
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<AccessLogVO>> create(@RequestBody AccessLogDTO accessLogDTO) {
        Mono<AccessLogVO> voMono;
        try {
            voMono = accessLogService.create(accessLogDTO);
        } catch (Exception e) {
            logger.error("Create record occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

}
