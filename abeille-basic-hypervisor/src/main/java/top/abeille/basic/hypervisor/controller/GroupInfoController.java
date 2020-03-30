/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.service.GroupInfoService;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.AbstractController;

/**
 * 组信息接口
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
     * @param pageNum  查询页码
     * @param pageSize 分页大小
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<Object> retrieveGroup(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<GroupVO> groups = groupInfoService.retrieveByPage(pageable);
        if (CollectionUtils.isEmpty(groups.getContent())) {
            logger.info("Not found anything about group with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 根据businessId查找组信息
     *
     * @param businessId 业务主键
     * @return ResponseEntity
     */
    @GetMapping("/{businessId}")
    public ResponseEntity<Object> fetchGroup(@PathVariable String businessId) {
        GroupVO groupInfo = groupInfoService.fetchByBusinessId(businessId);
        if (groupInfo == null) {
            logger.info("Not found anything about group with id {}.", businessId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groupInfo);
    }

    /**
     * 保存组信息
     *
     * @param groupDTO 组
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Object> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            groupInfoService.create(groupDTO);
        } catch (Exception e) {
            logger.error("Save group occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改组信息
     *
     * @param businessId 业务主键
     * @param groupDTO   组信息
     * @return ResponseEntity
     */
    @PutMapping("/{businessId}")
    public ResponseEntity<Object> modifyGroup(@PathVariable String businessId, @RequestBody GroupDTO groupDTO) {
        try {
            groupInfoService.modify(businessId, groupDTO);
        } catch (Exception e) {
            logger.error("Modify group occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除组信息
     *
     * @param businessId 业务主键
     * @return ResponseEntity
     */
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Object> removeGroup(@PathVariable String businessId) {
        try {
            groupInfoService.removeById(businessId);
        } catch (Exception e) {
            logger.error("Remove group occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
