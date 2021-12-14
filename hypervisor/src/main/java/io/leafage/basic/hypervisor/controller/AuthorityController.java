/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
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

/**
 * 权限资源接口
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    private final Logger logger = LoggerFactory.getLogger(AuthorityController.class);

    private final AuthorityService authorityService;
    private final RoleAuthorityService roleAuthorityService;

    public AuthorityController(AuthorityService authorityService, RoleAuthorityService roleAuthorityService) {
        this.authorityService = authorityService;
        this.roleAuthorityService = roleAuthorityService;
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
    public ResponseEntity<Page<AuthorityVO>> retrieve(@RequestParam int page, @RequestParam int size, String sort) {
        Page<AuthorityVO> authorities;
        try {
            authorities = authorityService.retrieve(page, size, sort);
        } catch (Exception e) {
            logger.info("Retrieve authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }

    /**
     * 查询树形数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/tree")
    public ResponseEntity<List<TreeNode>> tree() {
        List<TreeNode> authorities;
        try {
            authorities = authorityService.tree();
        } catch (Exception e) {
            logger.info("Retrieve authority tree occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 查询到的信息，否则返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<AuthorityVO> fetch(@PathVariable String code) {
        AuthorityVO authorityVO;
        try {
            authorityVO = authorityService.fetch(code);
        } catch (Exception e) {
            logger.info("Fetch authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorityVO);
    }

    /**
     * 添加信息
     *
     * @param authorityDTO 要添加的数据
     * @return 添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<AuthorityVO> create(@RequestBody @Valid AuthorityDTO authorityDTO) {
        AuthorityVO authorityVO;
        try {
            authorityVO = authorityService.create(authorityDTO);
        } catch (Exception e) {
            logger.error("Create authority occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authorityVO);
    }

    /**
     * 修改信息
     *
     * @param code         代码
     * @param authorityDTO 要添加的数据
     * @return 编辑后的信息，否则返回417状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<AuthorityVO> modify(@PathVariable String code, @RequestBody @Valid AuthorityDTO authorityDTO) {
        AuthorityVO authorityVO;
        try {
            authorityVO = authorityService.modify(code, authorityDTO);
        } catch (Exception e) {
            logger.error("Modify authority occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(authorityVO);
    }

    /**
     * 删除信息
     *
     * @param code 代码
     * @return 200状态码，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> remove(@PathVariable String code) {
        try {
            authorityService.remove(code);
        } catch (Exception e) {
            logger.error("Remove authority occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 查询关联角色
     *
     * @param code 代码
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{code}/role")
    public ResponseEntity<List<RoleVO>> roles(@PathVariable String code) {
        List<RoleVO> voList;
        try {
            voList = roleAuthorityService.roles(code);
        } catch (Exception e) {
            logger.error("Retrieve authority roles occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voList);
    }


}
