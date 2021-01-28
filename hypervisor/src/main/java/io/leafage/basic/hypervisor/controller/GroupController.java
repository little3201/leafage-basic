/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 组信息接口
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupController {

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public ResponseEntity<Object> retrieve(int page, int size) {
        Page<GroupVO> groups = groupService.retrieves(page, size);
        if (CollectionUtils.isEmpty(groups.getContent())) {
            logger.info("Not found anything about group with pageable.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Object> fetch(@PathVariable String code) {
        GroupVO groupInfo = groupService.fetch(code);
        if (groupInfo == null) {
            logger.info("Not found anything about group with code {}.", code);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(groupInfo);
    }

    /**
     * 添加信息
     *
     * @param groupDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody GroupDTO groupDTO) {
        try {
            groupService.create(groupDTO);
        } catch (Exception e) {
            logger.error("Save group occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改信息
     *
     * @param code     代码
     * @param groupDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Object> modify(@PathVariable String code, @RequestBody GroupDTO groupDTO) {
        try {
            groupService.modify(code, groupDTO);
        } catch (Exception e) {
            logger.error("Modify group occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除信息
     *
     * @param code 代码
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Object> remove(@PathVariable String code) {
        try {
            groupService.remove(code);
        } catch (Exception e) {
            logger.error("Remove group occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
