/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.entity.RoleAuthority;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.leafage.common.basic.TreeNode;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * role controller.
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@RestController
@RequestMapping("/role")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private final AccountRoleService accountRoleService;
    private final RoleService roleService;
    private final RoleAuthorityService roleAuthorityService;

    public RoleController(AccountRoleService accountRoleService, RoleService roleService, RoleAuthorityService roleAuthorityService) {
        this.accountRoleService = accountRoleService;
        this.roleService = roleService;
        this.roleAuthorityService = roleAuthorityService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public ResponseEntity<Page<RoleVO>> retrieve(@RequestParam int page, @RequestParam int size, String sort) {
        Page<RoleVO> roles;
        try {
            roles = roleService.retrieve(page, size, sort);
        } catch (Exception e) {
            logger.info("Retrieve role occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
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
            treeNodes = roleService.tree();
        } catch (Exception e) {
            logger.info("Retrieve role tree occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(treeNodes);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<RoleVO> fetch(@PathVariable String code) {
        RoleVO roleVO;
        try {
            roleVO = roleService.fetch(code);
        } catch (Exception e) {
            logger.info("Fetch role occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roleVO);
    }

    /**
     * 添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<RoleVO> create(@RequestBody @Valid RoleDTO roleDTO) {
        RoleVO roleVO;
        try {
            roleVO = roleService.create(roleDTO);
        } catch (Exception e) {
            logger.error("Create role occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleVO);
    }

    /**
     * 修改信息
     *
     * @param code    代码
     * @param roleDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<RoleVO> modify(@PathVariable String code, @RequestBody @Valid RoleDTO roleDTO) {
        RoleVO roleVO;
        try {
            roleVO = roleService.modify(code, roleDTO);
        } catch (Exception e) {
            logger.error("Modify role occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.accepted().body(roleVO);
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
            roleService.remove(code);
        } catch (Exception e) {
            logger.error("Remove role occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 根据code查询关联用户信息
     *
     * @param code 角色code
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{code}/account")
    public ResponseEntity<List<AccountVO>> accounts(@PathVariable String code) {
        List<AccountVO> voList;
        try {
            voList = accountRoleService.accounts(code);
        } catch (Exception e) {
            logger.error("Retrieve role users occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }

    /**
     * 查询角色-权限关联
     *
     * @param code 角色代码
     * @return 操作结果
     */
    @GetMapping("/{code}/authority")
    public ResponseEntity<List<AuthorityVO>> authorities(@PathVariable String code) {
        List<AuthorityVO> voList;
        try {
            voList = roleAuthorityService.authorities(code);
        } catch (Exception e) {
            logger.error("Relation role ah occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }

    /**
     * 保存角色-权限关联
     *
     * @param code        角色代码
     * @param authorities 权限信息
     * @return 操作结果
     */
    @PatchMapping("/{code}/authority")
    public ResponseEntity<List<RoleAuthority>> users(@PathVariable String code, @RequestBody Set<String> authorities) {
        List<RoleAuthority> voList;
        try {
            voList = roleAuthorityService.relation(code, authorities);
        } catch (Exception e) {
            logger.error("Relation role ah occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.accepted().body(voList);
    }

}
