/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

/**
 * notification controller
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Flux<NotificationVO>> retrieve(@RequestParam int page, @RequestParam int size, boolean read) {
        Flux<NotificationVO> voFlux;
        try {
            voFlux = notificationService.retrieve(page, size, read);
        } catch (Exception e) {
            logger.error("Retrieve notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 统计记录数
     *
     * @return 查询的记录数，异常时返回204状态码
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = notificationService.count();
        } catch (Exception e) {
            logger.error("Count notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 根据 code 查询
     *
     * @param code 代码
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<NotificationVO>> fetch(@PathVariable String code) {
        Mono<NotificationVO> voMono;
        try {
            voMono = notificationService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch notification occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 添加信息
     *
     * @param notificationDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<NotificationVO>> create(@RequestBody @Valid NotificationDTO notificationDTO) {
        Mono<NotificationVO> voMono;
        try {
            voMono = notificationService.create(notificationDTO);
        } catch (Exception e) {
            logger.error("Create notification occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改信息
     *
     * @param code            代码
     * @param notificationDTO 要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{username}")
    public ResponseEntity<Mono<NotificationVO>> modify(@PathVariable String code, @RequestBody @Valid NotificationDTO notificationDTO) {
        Mono<NotificationVO> voMono;
        try {
            voMono = notificationService.modify(code, notificationDTO);
        } catch (Exception e) {
            logger.error("Modify notification occurred an error: ", e);
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
            voidMono = notificationService.remove(code);
        } catch (Exception e) {
            logger.error("Remove notification occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(voidMono);
    }

}
