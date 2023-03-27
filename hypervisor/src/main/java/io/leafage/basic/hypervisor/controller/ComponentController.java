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

import io.leafage.basic.hypervisor.domain.RoleComponents;
import io.leafage.basic.hypervisor.dto.ComponentDTO;
import io.leafage.basic.hypervisor.service.ComponentService;
import io.leafage.basic.hypervisor.service.RoleComponentsService;
import io.leafage.basic.hypervisor.vo.ComponentVO;
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
 * component controller
 *
 * @author liwenqiang 2023-03-26 15:01
 **/
@RestController
@RequestMapping("/components")
public class ComponentController {

    private final Logger logger = LoggerFactory.getLogger(ComponentController.class);

    private final ComponentService componentService;
    private final RoleComponentsService roleComponentsService;

    public ComponentController(ComponentService componentService, RoleComponentsService roleComponentsService) {
        this.componentService = componentService;
        this.roleComponentsService = roleComponentsService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Mono<Page<ComponentVO>>> retrieve(@RequestParam int page, @RequestParam int size) {
        Mono<Page<ComponentVO>> pageMono;
        try {
            pageMono = componentService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve component occurred an error: ", e);
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
            authorities = componentService.tree();
        } catch (Exception e) {
            logger.info("Retrieve component occurred an error: ", e);
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
    public ResponseEntity<Mono<ComponentVO>> fetch(@PathVariable Long id) {
        Mono<ComponentVO> voMono;
        try {
            voMono = componentService.fetch(id);
        } catch (Exception e) {
            logger.error("Fetch component occurred an error: ", e);
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
            existsMono = componentService.exist(componentName);
        } catch (Exception e) {
            logger.error("Check component is exist an error: ", e);
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
    public ResponseEntity<Mono<ComponentVO>> create(@RequestBody @Valid ComponentDTO componentDTO) {
        Mono<ComponentVO> voMono;
        try {
            voMono = componentService.create(componentDTO);
        } catch (Exception e) {
            logger.error("Create component occurred an error: ", e);
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
    public ResponseEntity<Mono<ComponentVO>> modify(@PathVariable Long id, @RequestBody @Valid ComponentDTO componentDTO) {
        Mono<ComponentVO> voMono;
        try {
            voMono = componentService.modify(id, componentDTO);
        } catch (Exception e) {
            logger.error("Modify component occurred an error: ", e);
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
    public ResponseEntity<Mono<List<RoleComponents>>> roles(@PathVariable Long id) {
        Mono<List<RoleComponents>> listMono;
        try {
            listMono = roleComponentsService.roles(id);
        } catch (Exception e) {
            logger.error("Retrieve group users occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listMono);
    }

}
