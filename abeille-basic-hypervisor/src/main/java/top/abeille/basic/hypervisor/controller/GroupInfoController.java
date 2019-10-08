/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.service.GroupInfoService;
import top.abeille.basic.hypervisor.vo.enter.GroupEnter;
import top.abeille.basic.hypervisor.vo.outer.GroupOuter;
import top.abeille.common.basic.AbstractController;

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
    public Flux<ResponseEntity<GroupOuter>> fetchGroups() {
        return groupInfoService.findAll()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    /**
     * 保存组信息
     *
     * @param group 组
     * @return ResponseEntity
     */
    @PostMapping
    public Mono<ResponseEntity<GroupOuter>> saveGroup(@RequestBody GroupEnter group) {
        return groupInfoService.save(null, group)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 修改组信息
     *
     * @param group 组
     * @return ResponseEntity
     */
    @PutMapping("/{groupId}")
    public Mono<ResponseEntity<GroupOuter>> modifyGroup(@PathVariable Long groupId, @RequestBody GroupEnter group) {
        return groupInfoService.save(groupId, group)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 删除组信息
     *
     * @param groupId 业务主键
     * @return ResponseEntity
     */
    @DeleteMapping("/{groupId}")
    public Mono<ResponseEntity<Void>> removeGroup(@PathVariable Long groupId) {
        return groupInfoService.removeById(groupId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }
}
