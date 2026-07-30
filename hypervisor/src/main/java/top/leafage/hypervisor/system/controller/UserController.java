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
import top.leafage.common.poi.excel.ExcelReader;
import top.leafage.hypervisor.system.domain.dto.UserDTO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * User controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for UserController.
     *
     * @param userService a {@link UserService} object
     */
    public UserController(UserService userService) {
        this.userService = userService;
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
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users')")
    @GetMapping
    public ResponseEntity<Page<UserVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                 String sortBy, boolean descending, String filters) {
        Page<UserVO> voPage = userService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * fetch by id.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users')")
    @GetMapping("/{id}")
    public ResponseEntity<UserVO> fetch(@PathVariable Long id) {
        UserVO vo = userService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * create.
     *
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:create')")
    @PostMapping
    public ResponseEntity<UserVO> create(@Valid @RequestBody UserDTO dto) {
        UserVO vo = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * modify..
     *
     * @param id  the pk.
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:modify')")
    @PutMapping("/{id}")
    public ResponseEntity<UserVO> modify(@PathVariable Long id,
                                         @Valid @RequestBody UserDTO dto) {
        UserVO vo = userService.modify(id, dto);
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * Enable.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:enable')")
    @PatchMapping("/{id}/enable")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = userService.enable(id);
        return ResponseEntity.ok(enabled);
    }

    /**
     * Disable.
     *
     * @param id the pk.
     * @return 编辑后的信息，否则返回417状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:disable')")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Boolean> disable(@PathVariable Long id) {
        boolean disable = userService.disable(id);
        return ResponseEntity.ok(disable);
    }

    /**
     * Remove.
     *
     * @param id the pk.
     * @return no content.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        userService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * import.
     *
     * @param file the file of data.
     * @return the imported data.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:import')")
    @PostMapping("/import")
    public ResponseEntity<List<UserVO>> importFromFile(MultipartFile file) throws IOException {
        List<UserDTO> dtoList = ExcelReader.read(file.getInputStream(), UserDTO.class);
        List<UserVO> voList = userService.createAll(dtoList);

        return ResponseEntity.ok().body(voList);
    }

    /**
     * 根据user查询关联roles
     *
     * @param id user id
     * @return 查询到的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:authorize')")
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleVO>> roles(@PathVariable Long id) {
        List<RoleVO> roles = userService.roles(id);
        return ResponseEntity.ok(roles);
    }

    /**
     * 保存user-roles关联
     *
     * @param id      user id
     * @param roleIds role ids
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:authorize')")
    @PatchMapping("/{id}/roles")
    public ResponseEntity<Void> addRoles(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        userService.addRoles(id, roleIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除 user-roles关联
     *
     * @param id      user id
     * @param roleIds role ids
     * @return 操作结果
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('users:authorize')")
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<Void> removeRoles(@PathVariable Long id, @RequestParam Set<Long> roleIds) {
        userService.removeRoles(id, roleIds);
        return ResponseEntity.noContent().build();
    }

}
