/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
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
     * 查找组信息——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity getGroup(@PathVariable Long id) {
        GroupInfo groupInfo = groupInfoService.getById(id);
        if (groupInfo == null) {
            log.info("Not found anything about group with id {}.", id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groupInfo);
    }

    /**
     * 查找组信息——分页查询
     *
     * @param pageNum  查询页码
     * @param pageSize 分页大小
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity findGroups(Integer pageNum, Integer pageSize) {
        Page<GroupInfo> groups = groupInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(groups.getContent())) {
            log.info("Not found anything about group with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 保存组信息
     *
     * @param group 组
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity saveGroup(@RequestBody GroupInfo group) {
        try {
            groupInfoService.save(group);
        } catch (Exception e) {
            log.error("Save group occurred an error: ", e);
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
        try {
            groupInfoService.save(group);
        } catch (Exception e) {
            log.error("Modify group occurred an error: ", e);
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
        try {
            groupInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove group occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
