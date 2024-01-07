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

import io.leafage.basic.hypervisor.domain.RoleMembers;
import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.RoleMembersService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * role controller
 *
 * @author liwenqiang 2018/12/17 19:38
 **/
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private final RoleMembersService roleMembersService;
    private final RoleService roleService;
    private final RolePrivilegesService rolePrivilegesService;

    public RoleController(RoleMembersService roleMembersService, RoleService roleService,
                          RolePrivilegesService rolePrivilegesService) {
        this.roleMembersService = roleMembersService;
        this.roleService = roleService;
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
    public ResponseEntity<Mono<Page<RoleVO>>> retrieve(@RequestParam int page, @RequestParam int size) {
        Mono<Page<RoleVO>> pageMono;
        try {
            pageMono = roleService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve roles occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pageMono);
    }

    /**
     * 根据 id 查询信息
     *
     * @param id 主键
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mono<RoleVO>> fetch(@PathVariable Long id) {
        Mono<RoleVO> voMono;
        try {
            voMono = roleService.fetch(id);
        } catch (Exception e) {
            logger.error("Fetch role occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 是否已存在
     *
     * @param roleName 用户名
     * @return true-是，false-否
     */
    @GetMapping("/exist")
    public ResponseEntity<Mono<Boolean>> exist(@RequestParam String roleName) {
        Mono<Boolean> existsMono;
        try {
            existsMono = roleService.exist(roleName);
        } catch (Exception e) {
            logger.error("Check role is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(existsMono);
    }

    /**
     * 添加信息
     *
     * @param roleDTO 要添加的数据
     * @return 添加后的信息，否则返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<RoleVO>> create(@RequestBody @Valid RoleDTO roleDTO) {
        Mono<RoleVO> voMono;
        try {
            voMono = roleService.create(roleDTO);
        } catch (Exception e) {
            logger.error("Create role occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改信息
     *
     * @param id      主键
     * @param roleDTO 要修改的数据
     * @return 修改后的信息，否则返回304状态码
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mono<RoleVO>> modify(@PathVariable Long id, @RequestBody @Valid RoleDTO roleDTO) {
        Mono<RoleVO> voMono;
        try {
            voMono = roleService.modify(id, roleDTO);
        } catch (Exception e) {
            logger.error("Modify role occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 查询关联用户
     *
     * @param id 角色id
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{id}/members")
    public ResponseEntity<Mono<List<RoleMembers>>> members(@PathVariable Long id) {
        Mono<List<RoleMembers>> listMono;
        try {
            listMono = roleMembersService.members(id);
        } catch (Exception e) {
            logger.error("Retrieve role members occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listMono);
    }

    /**
     * 查询关联组件
     *
     * @param id 角色主键
     * @return 操作结果
     */
    @GetMapping("/{id}/privileges")
    public ResponseEntity<Mono<List<RolePrivileges>>> privileges(@PathVariable Long id) {
        Mono<List<RolePrivileges>> listMono;
        try {
            listMono = rolePrivilegesService.privileges(id);
        } catch (Exception e) {
            logger.error("Retrieve role privileges occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listMono);
    }

    /**
     * 关联
     *
     * @param id           role主键
     * @param privilegeIds privilege主键集合
     * @return 操作结果
     */
    @PatchMapping("/{id}/privileges")
    public ResponseEntity<Mono<Boolean>> relation(@PathVariable Long id, @RequestBody Set<Long> privilegeIds) {
        Mono<Boolean> voMono;
        try {
            voMono = rolePrivilegesService.relation(id, privilegeIds);
        } catch (Exception e) {
            logger.error("Relation role privileges occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

}
