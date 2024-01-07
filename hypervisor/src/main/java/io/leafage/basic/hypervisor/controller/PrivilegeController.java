/*
 *  Copyright 2018-2024 the original author or authors.
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

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.leafage.common.TreeNode;

import java.util.List;

/**
 * privilege controller
 *
 * @author liwenqiang 2023-03-26 15:01
 **/
@RestController
@RequestMapping("/privileges")
public class PrivilegeController {

    private final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

    private final PrivilegeService privilegeService;
    private final RolePrivilegesService rolePrivilegesService;

    public PrivilegeController(PrivilegeService privilegeService, RolePrivilegesService rolePrivilegesService) {
        this.privilegeService = privilegeService;
        this.rolePrivilegesService = rolePrivilegesService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Mono<Page<PrivilegeVO>>> retrieve(@RequestParam int page, @RequestParam int size) {
        Mono<Page<PrivilegeVO>> pageMono;
        try {
            pageMono = privilegeService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve privilege occurred an error: ", e);
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
            authorities = privilegeService.tree();
        } catch (Exception e) {
            logger.info("Retrieve privileges occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authorities);
    }

    /**
     * 根据 id 查询信息
     *
     * @param id 主键
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mono<PrivilegeVO>> fetch(@PathVariable Long id) {
        Mono<PrivilegeVO> voMono;
        try {
            voMono = privilegeService.fetch(id);
        } catch (Exception e) {
            logger.error("Fetch privilege occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 是否已存在
     *
     * @param componentName 名称
     * @return true-是，false-否
     */
    @GetMapping("/exist")
    public ResponseEntity<Mono<Boolean>> exist(@RequestParam String componentName) {
        Mono<Boolean> existsMono;
        try {
            existsMono = privilegeService.exist(componentName);
        } catch (Exception e) {
            logger.error("Check privilege is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(existsMono);
    }

    /**
     * 添加
     *
     * @param componentDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<PrivilegeVO>> create(@RequestBody @Valid PrivilegeDTO componentDTO) {
        Mono<PrivilegeVO> voMono;
        try {
            voMono = privilegeService.create(componentDTO);
        } catch (Exception e) {
            logger.error("Create privilege occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改
     *
     * @param id           主键
     * @param componentDTO 要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mono<PrivilegeVO>> modify(@PathVariable Long id, @RequestBody @Valid PrivilegeDTO componentDTO) {
        Mono<PrivilegeVO> voMono;
        try {
            voMono = privilegeService.modify(id, componentDTO);
        } catch (Exception e) {
            logger.error("Modify privilege occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 查询关联角色
     *
     * @param id 主键
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{id}/roles")
    public ResponseEntity<Mono<List<RolePrivileges>>> roles(@PathVariable Long id) {
        Mono<List<RolePrivileges>> listMono;
        try {
            listMono = rolePrivilegesService.roles(id);
        } catch (Exception e) {
            logger.error("Retrieve group users occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listMono);
    }

}
