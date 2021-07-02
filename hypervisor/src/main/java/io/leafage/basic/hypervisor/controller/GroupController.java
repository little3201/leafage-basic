/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.leafage.common.servlet.ServletTreeNode;
import java.util.List;

/**
 * 分组信息接口
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/group")
public class GroupController {

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final UserGroupService userGroupService;
    private final GroupService groupService;

    public GroupController(UserGroupService userGroupService, GroupService groupService) {
        this.userGroupService = userGroupService;
        this.groupService = groupService;
    }

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  大小
     * @param order 排序字段
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public ResponseEntity<Page<GroupVO>> retrieve(@RequestParam int page, @RequestParam int size, String order) {
        Page<GroupVO> groups;
        try {
            groups = groupService.retrieve(page, size, order);
        } catch (Exception e) {
            logger.info("Retrieve group occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 查询树形数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/tree")
    public ResponseEntity<List<ServletTreeNode>> tree() {
        List<ServletTreeNode> authorities;
        try {
            authorities = groupService.tree();
        } catch (Exception e) {
            logger.info("Retrieve group tree occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<GroupVO> fetch(@PathVariable String code) {
        GroupVO groupVO;
        try {
            groupVO = groupService.fetch(code);
        } catch (Exception e) {
            logger.info("Fetch group occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(groupVO);
    }

    /**
     * 添加信息
     *
     * @param groupDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<GroupVO> create(@RequestBody GroupDTO groupDTO) {
        GroupVO groupVO;
        try {
            groupVO = groupService.create(groupDTO);
        } catch (Exception e) {
            logger.error("Create group occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(groupVO);
    }

    /**
     * 修改信息
     *
     * @param code     代码
     * @param groupDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<GroupVO> modify(@PathVariable String code, @RequestBody GroupDTO groupDTO) {
        GroupVO groupVO;
        try {
            groupVO = groupService.modify(code, groupDTO);
        } catch (Exception e) {
            logger.error("Modify group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(groupVO);
    }

    /**
     * 删除信息
     *
     * @param code 代码
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> remove(@PathVariable String code) {
        try {
            groupService.remove(code);
        } catch (Exception e) {
            logger.error("Remove group occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 根据分组code查询关联用户信息
     *
     * @param code 组code
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{code}/user")
    public ResponseEntity<List<UserVO>> users(@PathVariable String code) {
        List<UserVO> voList;
        try {
            voList = userGroupService.users(code);
        } catch (Exception e) {
            logger.error("Retrieve group users occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }
}
