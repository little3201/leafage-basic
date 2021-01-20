/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.hypervisor.dto.AuthorityDTO;
import top.abeille.basic.hypervisor.service.AuthorityService;
import top.abeille.basic.hypervisor.vo.AuthorityVO;

import javax.validation.Valid;

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

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
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
        Page<AuthorityVO> sources = authorityService.retrieves(page, size);
        if (CollectionUtils.isEmpty(sources.getContent())) {
            logger.info("Not found anything about source with pageable.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sources);
    }

    /**
     * 添加信息
     *
     * @param authorityDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorityDTO authorityDTO) {
        try {
            authorityService.create(authorityDTO);
        } catch (Exception e) {
            logger.error("Save user occurred an error: ", e);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

}
