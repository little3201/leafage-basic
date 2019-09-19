/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.entity.SourceInfo;
import top.abeille.basic.hypervisor.service.SourceInfoService;
import top.abeille.common.basic.AbstractController;

import java.util.Objects;

/**
 * 权限资源controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/source")
public class SourceInfoController extends AbstractController {

    private final SourceInfoService sourceInfoService;

    public SourceInfoController(SourceInfoService sourceInfoService) {
        this.sourceInfoService = sourceInfoService;
    }

    /**
     * 查找资源信息
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity fetchSources() {
        Flux<SourceInfo> infoFlux = sourceInfoService.findAll();
        if (Objects.isNull(infoFlux)) {
            log.info("Not found anything about source.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(infoFlux);
    }
}
