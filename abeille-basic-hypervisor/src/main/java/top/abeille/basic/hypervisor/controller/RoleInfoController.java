/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.basic.hypervisor.service.RoleInfoService;
import top.abeille.common.basic.AbstractController;

import java.util.List;

/**
 * 角色信息controller
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@RestController
@RequestMapping("/role")
public class RoleInfoController extends AbstractController {

    private final RoleInfoService roleInfoService;

    public RoleInfoController(RoleInfoService roleInfoService) {
        this.roleInfoService = roleInfoService;
    }

    /**
     * 角色查询——分页
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity findRoles(Integer pageNum, Integer pageSize) {
        if(null == pageNum){
            pageNum = 0;
        }
        if(null == pageSize){
            pageSize = 10;
        }
        Page<RoleInfo> roles = roleInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(roles.getContent())) {
            log.info("Not found anything about role with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(roles);
    }

    /**
     * 保存角色
     *
     * @param role 角色
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity saveRole(@RequestBody RoleInfo role) {
        try {
            roleInfoService.save(role);
        } catch (Exception e) {
            log.error("Save role occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 编辑角色
     *
     * @param role 角色
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity modifyRole(@RequestBody RoleInfo role) {
        try {
            roleInfoService.save(role);
        } catch (Exception e) {
            log.error("Modify role occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除角色——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity removeRole(@PathVariable Long id) {
        try {
            roleInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove role occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
