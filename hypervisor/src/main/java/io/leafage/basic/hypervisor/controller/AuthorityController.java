/*
 *  Copyright 2018-2023 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.service.RoleAuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;

import java.util.List;


/**
 * authority controller
 *
 * @author liwenqiang 2018/12/17 19:39
 **/
@RestController
@RequestMapping("/authorities")
public class AuthorityController {

    private final Logger logger = LoggerFactory.getLogger(AuthorityController.class);

    private final RoleAuthorityService roleAuthorityService;
    private final AuthorityService authorityService;

    public AuthorityController(RoleAuthorityService roleAuthorityService, AuthorityService authorityService) {
        this.roleAuthorityService = roleAuthorityService;
        this.authorityService = authorityService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Mono<Page<AuthorityVO>>> retrieve(@RequestParam int page, @RequestParam int size) {
        Mono<Page<AuthorityVO>> pageMono;
        try {
            pageMono = authorityService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pageMono);
    }

    /**
     * 树形查询
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/tree")
    public ResponseEntity<Mono<List<TreeNode>>> tree() {
        Mono<List<TreeNode>> authorities;
        try {
            authorities = authorityService.tree();
        } catch (Exception e) {
            logger.info("Retrieve authority occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }

    /**
     * 根据 code 查询信息
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
     * 添加信息
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
     * 修改信息
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
     * 查询关联角色
     *
     * @param code 代码
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{code}/roles")
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
