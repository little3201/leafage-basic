/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.abeille.basic.hypervisor.entity.SourceInfo;
import top.abeille.basic.hypervisor.service.SourceInfoService;
import top.abeille.common.basic.AbstractController;

import javax.validation.constraints.NotNull;

/**
 * 权限资源controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@Api(tags = "Source Api")
@RestController
@RequestMapping("/source")
public class SourceInfoController extends AbstractController {

    private final SourceInfoService sourceInfoService;

    public SourceInfoController(SourceInfoService sourceInfoService) {
        this.sourceInfoService = sourceInfoService;
    }

    /**
     * 权限查询——分页
     *
     * @param pageNum  当前页
     * @param pageSize 页内数据量
     * @return ResponseEntity
     */
    @ApiOperation(value = "Fetch enabled sources with pageable")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity findSource(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        Page<SourceInfo> sources = sourceInfoService.findAllByPage(pageNum, pageSize);
        if (CollectionUtils.isEmpty(sources.getContent())) {
            log.info("Not found anything about source with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(sources);
    }

}
