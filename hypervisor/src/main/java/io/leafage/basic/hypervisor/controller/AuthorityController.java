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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.TreeNode;
import javax.validation.Valid;

/**
 * 权限 controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    private final Logger logger = LoggerFactory.getLogger(AuthorityController.class);

    private final RoleAuthorityService roleAuthorityService;
    private final AuthorityService authorityService;

    public AuthorityController(RoleAuthorityService roleAuthorityService, AuthorityService authorityService) {
        this.roleAuthorityService = roleAuthorityService;
        this.authorityService = authorityService;
    }

    /**
     * 查询资源信息
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Flux<AuthorityVO>> retrieve(Integer page, Integer size) {
        Flux<AuthorityVO> voFlux;
        try {
            if (page == null || size == null) {
                voFlux = authorityService.retrieve();
            } else {
                voFlux = authorityService.retrieve(page, size);
            }
        } catch (Exception e) {
            logger.error("Retrieve authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 查询树形数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/tree")
    public ResponseEntity<Flux<TreeNode>> tree() {
        Flux<TreeNode> authorities;
        try {
            authorities = authorityService.tree();
        } catch (Exception e) {
            logger.info("Retrieve authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<AuthorityVO>> fetch(@PathVariable String code) {
        Mono<AuthorityVO> voMono;
        try {
            voMono = authorityService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 统计记录数
     *
     * @return 记录数
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = authorityService.count();
        } catch (Exception e) {
            logger.error("Count authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 是否已存在
     *
     * @param name 用户名
     * @return true-是，false-否
     */
    @GetMapping("/exist")
    public ResponseEntity<Mono<Boolean>> exist(@RequestParam String name) {
        Mono<Boolean> existsMono;
        try {
            existsMono = authorityService.exist(name);
        } catch (Exception e) {
            logger.error("Check authority is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(existsMono);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param authorityDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<AuthorityVO>> create(@RequestBody @Valid AuthorityDTO authorityDTO) {
        Mono<AuthorityVO> voMono;
        try {
            voMono = authorityService.create(authorityDTO);
        } catch (Exception e) {
            logger.error("Create authority occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code         代码
     * @param authorityDTO 要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<AuthorityVO>> modify(@PathVariable String code, @RequestBody @Valid AuthorityDTO authorityDTO) {
        Mono<AuthorityVO> voMono;
        try {
            voMono = authorityService.modify(code, authorityDTO);
        } catch (Exception e) {
            logger.error("Modify authority occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 根据code查询关联角色信息
     *
     * @param code 代码
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{code}/role")
    public ResponseEntity<Flux<RoleVO>> roles(@PathVariable String code) {
        Flux<RoleVO> voFlux;
        try {
            voFlux = roleAuthorityService.roles(code);
        } catch (Exception e) {
            logger.error("Retrieve group users occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

}
