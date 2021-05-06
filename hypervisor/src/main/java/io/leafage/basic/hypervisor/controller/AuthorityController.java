/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<AuthorityVO>> retrieve(@RequestParam int page, @RequestParam int size, String order) {
        Page<AuthorityVO> authorities;
        try {
            authorities = authorityService.retrieve(page, size, order);
        } catch (Exception e) {
            logger.info("Retrieve authority occurred an error: ", e);
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
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
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
     * @param authorityDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Object> modify(@PathVariable String code, @RequestBody @Valid AuthorityDTO authorityDTO) {
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
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Object> remove(@PathVariable String code) {
        try {
            authorityService.remove(code);
        } catch (Exception e) {
            logger.error("Remove authority occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }

}
