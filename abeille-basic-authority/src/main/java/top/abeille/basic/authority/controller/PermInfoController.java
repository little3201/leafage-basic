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

}
