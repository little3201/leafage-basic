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
     * 查找组信息——分页查询
     *
     * @return ResponseEntity
     */
    @GetMapping
    public Flux<GroupInfo> fetchGroups() {
        return groupInfoService.findAll();
    }

    /**
     * 保存组信息
     *
     * @param group 组
     * @return ResponseEntity
     */
    @PostMapping
    public Mono<GroupInfo> saveGroup(@RequestBody GroupInfo group) {
        return groupInfoService.save(group);
    }

    /**
     * 修改组信息
     *
     * @param group 组
     * @return ResponseEntity
     */
    @PutMapping
    public Mono<GroupInfo> modifyGroup(@RequestBody GroupInfo group) {
        return groupInfoService.save(group);
    }

    /**
     * 删除组信息
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public Mono<Void> removeGroup(@PathVariable Long id) {
        return groupInfoService.removeById(id);
    }
}
