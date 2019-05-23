/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.authority.model.PermInfoModel;
import top.abeille.basic.authority.service.PermInfoService;
import top.abeille.common.basic.BasicController;

/**
 * 权限资源controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@Api(tags = "Permission Api")
@RestController
public class PermInfoController extends BasicController {

    private final PermInfoService permInfoService;

    public PermInfoController(PermInfoService permInfoService) {
        this.permInfoService = permInfoService;
    }

    /**
     * 权限查询——分页
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return ResponseEntity
     */
    @ApiOperation(value = "Fetch enabled permissions with pageable")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/permissions")
    public ResponseEntity findPermissions(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Page<PermInfoModel> permissions = permInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(permissions.getContent())) {
            log.info("Not found anything about permission with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(permissions);
    }

    /**
     * 权限查询——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @ApiOperation(value = "Get single permission by id")
    @ApiImplicitParam(name = "id", required = true)
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/permission/{id}")
    public ResponseEntity getPermission(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        PermInfoModel permission = permInfoService.getById(id);
        if (permission == null) {
            log.info("Not found anything of permission with id: {}.", id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(permission);
    }

    /**
     * 保存权限
     *
     * @param permission 权限
     * @return ResponseEntity
     */
    @ApiOperation(value = "Save single permission")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/permission")
    public ResponseEntity savePermission(@RequestBody PermInfoModel permission) {
        try {
            permInfoService.save(permission);
        } catch (Exception e) {
            log.error("Save permission occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 编辑权限
     *
     * @param permission 权限
     * @return ResponseEntity
     */
    @ApiOperation(value = "Modify single permission")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PutMapping("/permission")
    public ResponseEntity modifyPermission(@RequestBody PermInfoModel permission) {
        if (permission.getId() == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            permInfoService.save(permission);
        } catch (Exception e) {
            log.error("Modify permission occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除权限——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @ApiOperation(value = "Remove single permission")
    @ApiImplicitParam(name = "id", required = true)
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/permission/{id}")
    public ResponseEntity removePermission(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            permInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove permission occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
