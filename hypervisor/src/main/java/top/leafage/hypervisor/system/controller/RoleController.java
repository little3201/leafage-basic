/*
 * Copyright (c) 2024-2025.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.leafage.hypervisor.system.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.leafage.common.poi.excel.ExcelReader;
import top.leafage.hypervisor.system.domain.RolePrivilege;
import top.leafage.hypervisor.system.domain.User;
import top.leafage.hypervisor.system.domain.dto.RoleDTO;
import top.leafage.hypervisor.system.domain.vo.RolePrivilegeVO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.service.RoleService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * role controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    /**
     * Constructor for RoleController.
     *
     * @param roleService a {@link RoleService} object
     */
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Retrieves a paginated list of records.
     *
     * @param page       The page number.
     * @param size       The number of records per page.
     * @param sortBy     The field to sort by.
     * @param descending Whether sorting should be in descending order.
     * @param filters    The filters.
     * @return A paginated list of records, or 204 status code if an error occurs.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles')")
    @GetMapping
    public ResponseEntity<Page<RoleVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                 String sortBy, boolean descending, String filters) {
        Page<RoleVO> voPage = roleService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * fetch by id.
     *
     * @param id the pk.
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleVO> fetch(@PathVariable Long id) {
        RoleVO vo = roleService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * create.
     *
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:create')")
    @PostMapping
    public ResponseEntity<RoleVO> create(@Valid @RequestBody RoleDTO dto) {
        RoleVO vo = roleService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * modify.
     *
     * @param id  the pk.
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:modify')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleVO> modify(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        RoleVO vo = roleService.modify(id, dto);
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * remove.
     *
     * @param id the pk.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        roleService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * enable.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasAuthority('SCOPE_roles:enable')")
    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = roleService.enable(id);
        return ResponseEntity.ok(enabled);
    }

    /**
     * import.
     *
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:import')")
    @PostMapping("/import")
    public ResponseEntity<List<RoleVO>> importFromFile(MultipartFile file) throws IOException {
        List<RoleDTO> dtoList = ExcelReader.read(file.getInputStream(), RoleDTO.class);
        List<RoleVO> voList = roleService.createAll(dtoList);

        return ResponseEntity.ok().body(voList);
    }

    /**
     * 保存role-privilege关联
     *
     * @param id        role id
     * @param usernames 账号
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:relation')")
    @PatchMapping("/{id}/members")
    public ResponseEntity<Void> addMembers(@PathVariable Long id, @RequestBody Set<String> usernames) {
        roleService.addMembers(id, usernames);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除 role-privilege关联
     *
     * @param id        the pk of role.
     * @param usernames username集合
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:relation')")
    @DeleteMapping("/{id}/members")
    public ResponseEntity<Void> removeMembers(@PathVariable Long id, @RequestParam Set<String> usernames) {
        roleService.removeMembers(id, usernames);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据id查询关联用户信息
     *
     * @param id roleid
     * @return 查询到的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:relation')")
    @GetMapping("/{id}/members")
    public ResponseEntity<List<UserVO>> members(@PathVariable Long id) {
        List<UserVO> members = roleService.members(id);
        return ResponseEntity.ok(members);
    }

    /**
     * 保存role-privilege关联
     *
     * @param id          role id
     * @param privilegeId privilege id
     * @param action      操作
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:authorize')")
    @PatchMapping("/{id}/privileges/{privilegeId}")
    public ResponseEntity<Void> addPrivilege(@PathVariable Long id, @PathVariable Long privilegeId,
                                             String action) {
        roleService.addPrivilege(id, privilegeId, action);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询role-privilege关联
     *
     * @param id role代码
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:authorize')")
    @GetMapping("/{id}/privileges")
    public ResponseEntity<List<RolePrivilegeVO>> privileges(@PathVariable Long id) {
        List<RolePrivilegeVO> privileges = roleService.privileges(id);
        return ResponseEntity.ok(privileges);
    }

    /**
     * 移除 privilege
     *
     * @param id          the pk of role.
     * @param privilegeId the pk of privilege.
     * @param action      the action of privilege.
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_roles:authorize')")
    @DeleteMapping("/{id}/privileges/{privilegeId}")
    public ResponseEntity<Void> removePrivilege(@PathVariable Long id, @PathVariable Long privilegeId,
                                                String action) {
        roleService.removePrivilege(id, privilegeId, action);
        return ResponseEntity.noContent().build();
    }
}
