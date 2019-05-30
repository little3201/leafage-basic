/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.profile.entity.GroupInfo;
import top.abeille.basic.profile.service.GroupInfoService;
import top.abeille.common.basic.AbstractController;

/**
 * 组织信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
public class GroupInfoController extends AbstractController {

    private final GroupInfoService groupInfoService;

    public GroupInfoController(GroupInfoService groupInfoService) {
        this.groupInfoService = groupInfoService;
    }

    /**
     * 查找组织信息——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @GetMapping("/group/{id}")
    public ResponseEntity getGroup(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        GroupInfo groupInfo = groupInfoService.getById(id);
        if (groupInfo == null) {
            log.info("Not found anything about group with id {}.", id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groupInfo);
    }

    /**
     * 查找组织信息——分页查询
     *
     * @param pageNum  查询页码
     * @param pageSize 分页大小
     * @return ResponseEntity
     */
    @GetMapping("/groups")
    public ResponseEntity findGroups(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Page<GroupInfo> groups = groupInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(groups.getContent())) {
            log.info("Not found anything about group with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 保存组织信息
     *
     * @param group 组织
     * @return ResponseEntity
     */
    @PostMapping("/group")
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
     * 修改组织信息
     *
     * @param group 组织
     * @return ResponseEntity
     */
    @PutMapping("/group")
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
     * 删除组织信息
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/group/{id}")
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
