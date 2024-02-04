/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.leafage.common.TreeNode;

import java.util.List;

/**
 * privilege controller.
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/privileges")
public class PrivilegeController {

    private final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

    private final PrivilegeService privilegeService;
    private final RolePrivilegesService rolePrivilegesService;

    public PrivilegeController(PrivilegeService privilegeService, RolePrivilegesService rolePrivilegesService) {
        this.privilegeService = privilegeService;
        this.rolePrivilegesService = rolePrivilegesService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询到的数据，否则返回空
     */
    @GetMapping
    public ResponseEntity<Page<PrivilegeVO>> retrieve(@RequestParam int page, @RequestParam int size, String sort) {
        Page<PrivilegeVO> voPage;
        try {
            voPage = privilegeService.retrieve(page, size, sort);
        } catch (Exception e) {
            logger.info("Retrieve privilege occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 查询树形数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/tree")
    public ResponseEntity<List<TreeNode>> tree() {
        List<TreeNode> treeNodes;
        try {
            treeNodes = privilegeService.tree();
        } catch (Exception e) {
            logger.info("Retrieve privilege tree occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(treeNodes);
    }

    /**
     * 查询信息
     *
     * @param id 主键
     * @return 查询到的信息，否则返回204状态码
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrivilegeVO> fetch(@PathVariable Long id) {
        PrivilegeVO privilegeVO;
        try {
            privilegeVO = privilegeService.fetch(id);
        } catch (Exception e) {
            logger.info("Fetch privilege occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(privilegeVO);
    }

    /**
     * 添加信息
     *
     * @param privilegeDTO 要添加的数据
     * @return 添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<PrivilegeVO> create(@RequestBody @Valid PrivilegeDTO privilegeDTO) {
        PrivilegeVO privilegeVO;
        try {
            privilegeVO = privilegeService.create(privilegeDTO);
        } catch (Exception e) {
            logger.error("Create privilege occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(privilegeVO);
    }

    /**
     * 修改信息
     *
     * @param id           主键
     * @param privilegeDTO 要添加的数据
     * @return 编辑后的信息，否则返回417状态码
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrivilegeVO> modify(@PathVariable Long id, @RequestBody @Valid PrivilegeDTO privilegeDTO) {
        PrivilegeVO privilegeVO;
        try {
            privilegeVO = privilegeService.modify(id, privilegeDTO);
        } catch (Exception e) {
            logger.error("Modify privilege occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(privilegeVO);
    }

    /**
     * 删除信息
     *
     * @param id 主键
     * @return 200状态码，否则返回417状态码
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        try {
            privilegeService.remove(id);
        } catch (Exception e) {
            logger.error("Remove privilege occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 查询关联role
     *
     * @param id 主键
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RolePrivileges>> roles(@PathVariable Long id) {
        List<RolePrivileges> voList;
        try {
            voList = rolePrivilegesService.roles(id);
        } catch (Exception e) {
            logger.error("Retrieve privilege roles occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }


}
