/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.abeille.basic.hypervisor.service.SourceInfoService;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.AbstractController;

/**
 * 权限资源接口
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
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
    @GetMapping
    public ResponseEntity<Object> retrieveSource(Integer pageNum, Integer pageSize) {
        Pageable pageable = super.initPageParams(pageNum, pageSize);
        Page<SourceVO> sources = sourceInfoService.retrieveByPage(pageable);
        if (CollectionUtils.isEmpty(sources.getContent())) {
            logger.info("Not found anything about source with pageable.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(sources);
    }

}
