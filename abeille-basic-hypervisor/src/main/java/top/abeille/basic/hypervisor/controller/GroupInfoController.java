/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.GroupInfo;
import top.abeille.basic.hypervisor.service.GroupInfoService;
import top.abeille.common.basic.AbstractController;

import java.util.Objects;

/**
 * 组信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupInfoController extends AbstractController {

    private final GroupInfoService groupInfoService;

    public GroupInfoController(GroupInfoService groupInfoService) {
        this.groupInfoService = groupInfoService;
    }

    /**
     * 查找组信息
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity fetchGroups() {
        Flux<GroupInfo> infoFlux = groupInfoService.findAll();
        if (Objects.isNull(infoFlux)) {
            log.info("Not found anything about groups.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(infoFlux);
    }

    /**
     * 保存组信息
     *
     * @param group 组
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity saveGroup(@RequestBody GroupInfo group) {
        Mono<GroupInfo> infoMono = groupInfoService.save(group);
        if (Objects.isNull(infoMono)) {
            log.error("Save group occurred error.");
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改组信息
     *
     * @param group 组
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity modifyGroup(@RequestBody GroupInfo group) {
        if (Objects.isNull(group.getId())) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Mono<GroupInfo> infoMono = groupInfoService.save(group);
        if (Objects.isNull(infoMono)) {
            log.error("Modify group occurred error.");
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除组信息
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity removeGroup(@PathVariable Long id) {
        Mono<Void> voidMono = groupInfoService.removeById(id);
        if (Objects.isNull(voidMono)) {
            log.error("Remove group occurred error.");
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(voidMono);
    }
}
