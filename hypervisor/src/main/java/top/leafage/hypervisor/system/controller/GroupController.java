/*
 * Copyright(c) 2019-present the original author or authors.
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
import top.leafage.common.data.core.domain.TreeNode;
import top.leafage.common.poi.excel.ExcelReader;
import top.leafage.hypervisor.system.domain.dto.GroupDTO;
import top.leafage.hypervisor.system.domain.vo.GroupVO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.SimplePrivilegeVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.service.GroupService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Group controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    /**
     * Constructor for GroupController.
     *
     * @param groupService a {@link GroupService} object
     */
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
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
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups')")
    @GetMapping
    public ResponseEntity<Page<GroupVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                  String sortBy, boolean descending, String filters) {
        Page<GroupVO> voPage = groupService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * 查询树形数据
     *
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups')")
    @GetMapping("/tree")
    public ResponseEntity<List<TreeNode<Long>>> tree() {
        List<TreeNode<Long>> treeNodes = groupService.tree();
        return ResponseEntity.ok(treeNodes);
    }

    /**
     * fetch by id.
     *
     * @param id the pk.
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups')")
    @GetMapping("/{id}")
    public ResponseEntity<GroupVO> fetch(@PathVariable Long id) {
        GroupVO vo = groupService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * create.
     *
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:create')")
    @PostMapping
    public ResponseEntity<GroupVO> create(@Valid @RequestBody GroupDTO dto) {
        GroupVO vo = groupService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * modify.
     *
     * @param id  the pk.
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:modify')")
    @PutMapping("/{id}")
    public ResponseEntity<GroupVO> modify(@PathVariable Long id, @RequestBody GroupDTO dto) {
        GroupVO vo = groupService.modify(id, dto);
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * Remove.
     *
     * @param id the pk.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        groupService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * enable.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:enable')")
    @PatchMapping("/{id}/enable")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = groupService.enable(id);
        return ResponseEntity.ok(enabled);
    }

    /**
     * disable.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:disable')")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Boolean> disable(@PathVariable Long id) {
        boolean disable = groupService.disable(id);
        return ResponseEntity.ok(disable);
    }

    /**
     * import.
     *
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:import')")
    @PostMapping("/import")
    public ResponseEntity<List<GroupVO>> importFromFile(MultipartFile file) throws IOException {
        List<GroupDTO> dtoList = ExcelReader.read(file.getInputStream(), GroupDTO.class);
        List<GroupVO> voList = groupService.createAll(dtoList);

        return ResponseEntity.ok().body(voList);
    }

    /**
     * 保存group-users关联
     *
     * @param id        group id
     * @param usernames 账号
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:relation')")
    @PatchMapping("/{id}/members")
    public ResponseEntity<Void> addMembers(@PathVariable Long id, @RequestBody Set<String> usernames) {
        groupService.addMembers(id, usernames);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除 group-users关联
     *
     * @param id        the pk of group.
     * @param usernames username集合
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:member')")
    @DeleteMapping("/{id}/members")
    public ResponseEntity<Void> removeMembers(@PathVariable Long id, @RequestParam Set<String> usernames) {
        groupService.removeMembers(id, usernames);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据group查询关联user
     *
     * @param id group id
     * @return 查询到的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:member')")
    @GetMapping("/{id}/members")
    public ResponseEntity<List<UserVO>> members(@PathVariable Long id) {
        List<UserVO> members = groupService.members(id);
        return ResponseEntity.ok(members);
    }

    /**
     * 保存group-roles关联
     *
     * @param id      group id
     * @param roleIds role ids
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:authorize')")
    @PatchMapping("/{id}/roles")
    public ResponseEntity<Void> addRoles(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        groupService.addRoles(id, roleIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据group查询关联roles
     *
     * @param id group id
     * @return 查询到的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:authorize')")
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleVO>> roles(@PathVariable Long id) {
        List<RoleVO> roles = groupService.roles(id);
        return ResponseEntity.ok(roles);
    }

    /**
     * 删除 group-roles关联
     *
     * @param id      group id
     * @param roleIds role ids
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:authorize')")
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<Void> removeRoles(@PathVariable Long id, @RequestParam Set<Long> roleIds) {
        groupService.removeRoles(id, roleIds);
        return ResponseEntity.noContent().build();
    }

    /**
     * 添加 privilege
     *
     * @param id          role id
     * @param privilegeId privilege id
     * @param action      操作
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:authorize')")
    @PatchMapping("/{id}/privileges/{privilegeId}")
    public ResponseEntity<Void> addPrivilege(@PathVariable Long id, @PathVariable Long privilegeId,
                                             String action) {
        groupService.addPrivilege(id, privilegeId, action);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询 privilege
     *
     * @param id role代码
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:authorize')")
    @GetMapping("/{id}/privileges")
    public ResponseEntity<List<SimplePrivilegeVO>> privileges(@PathVariable Long id) {
        List<SimplePrivilegeVO> privileges = groupService.privileges(id);
        return ResponseEntity.ok(privileges);
    }

    /**
     * 删除 privilege
     *
     * @param id          group id
     * @param privilegeId privilege id
     * @param action      操作
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('SCOPE_groups:authorize')")
    @DeleteMapping("/{id}/privileges/{privilegeId}")
    public ResponseEntity<Void> removePrivilege(@PathVariable Long id, @PathVariable Long privilegeId,
                                                String action) {
        groupService.removePrivilege(id, privilegeId, action);
        return ResponseEntity.noContent().build();
    }
}
