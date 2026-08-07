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

package top.leafage.hypervisor.schedulers.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.leafage.hypervisor.schedulers.domain.dto.SchedulerDTO;
import top.leafage.hypervisor.schedulers.domain.vo.SchedulerVO;
import top.leafage.hypervisor.schedulers.service.SchedulerService;

/**
 * Scheduler controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/schedulers")
public class SchedulerController {

    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
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
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('schedulers')")
    @GetMapping
    public ResponseEntity<Page<SchedulerVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                      String sortBy, boolean descending, String filters) {
        Page<SchedulerVO> voPage = schedulerService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * Fetches a record by ID.
     *
     * @param id The pk.
     * @return The record data, or 204 status code if an error occurs.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('schedulers')")
    @GetMapping("/{id}")
    public ResponseEntity<SchedulerVO> fetch(@PathVariable Long id) {
        SchedulerVO vo = schedulerService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * create.
     *
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('schedulers:create')")
    @PostMapping
    public ResponseEntity<SchedulerVO> create(@Valid @RequestBody SchedulerDTO dto) {
        SchedulerVO vo = schedulerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * modify.
     *
     * @param dto the request body.
     * @param id  The pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('schedulers:modify')")
    @PutMapping("/{id}")
    public ResponseEntity<SchedulerVO> modify(@PathVariable Long id, @Valid @RequestBody SchedulerDTO dto) {
        SchedulerVO vo = schedulerService.modify(id, dto);
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * Removes a record by ID.
     *
     * @param id The pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('schedulers:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        schedulerService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Enable.
     *
     * @param id The pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('schedulers:enable')")
    @PatchMapping("/{id}/enable")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = schedulerService.enable(id);
        return ResponseEntity.ok(enabled);
    }

    /**
     * Disable.
     *
     * @param id The pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('ADMIN') || hasAuthority('schedulers:disable')")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Boolean> disable(@PathVariable Long id) {
        boolean disable = schedulerService.disable(id);
        return ResponseEntity.ok(disable);
    }

}
