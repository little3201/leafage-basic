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
package top.leafage.hypervisor.audits.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.leafage.hypervisor.audits.service.PatrolService;
import top.leafage.hypervisor.system.domain.vo.UserVO;
import top.leafage.hypervisor.system.service.UserService;

/**
 * Patrol controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping(value = "/audit-patrols")
public class PatrolController {

    private final PatrolService patrolService;
    private final UserService userService;

    /**
     * Constructor for PatrolController.
     *
     * @param patrolService a {@link PatrolService} object
     * @param userService   a {@link UserService} object
     */
    public PatrolController(PatrolService patrolService, UserService userService) {
        this.patrolService = patrolService;
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
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('partols')")
    @GetMapping
    public ResponseEntity<Page<UserVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                 String sortBy, boolean descending, String filters) {
        Page<UserVO> voPage = patrolService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * fetch by id.
     *
     * @param id The pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('partols')")
    @GetMapping("/{id}")
    public ResponseEntity<UserVO> fetch(@PathVariable Long id) {
        UserVO vo = userService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * Enable.
     *
     * @param id The pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('partols:enable')")
    @PatchMapping("/{id}/enable")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = userService.enable(id);
        return ResponseEntity.ok(enabled);
    }

    /**
     * Disable.
     *
     * @param id The pk.
     * @return 编辑后的信息，否则返回417状态码
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('partols:disable')")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Boolean> disable(@PathVariable Long id) {
        boolean disable = userService.disable(id);
        return ResponseEntity.ok(disable);
    }

    /**
     * Remove.
     *
     * @param id The pk.
     * @return no content.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('partols:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        userService.remove(id);
        return ResponseEntity.noContent().build();
    }

}
