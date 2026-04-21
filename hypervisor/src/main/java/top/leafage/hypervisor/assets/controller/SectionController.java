/*
 * Copyright (c) 2026.  little3201.
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

package top.leafage.hypervisor.assets.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionFieldVO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.service.SectionService;

import java.util.List;

/**
 * section controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }


    /**
     * fetch.
     *
     * @param id th pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections')")
    @GetMapping("/{id}")
    public ResponseEntity<SectionVO> fetch(@PathVariable Long id) {
        SectionVO vo = sectionService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * subset.
     *
     * @param id th pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections')")
    @GetMapping("/subset")
    public ResponseEntity<List<SectionVO>> subset(Long id) {
        List<SectionVO> voList = sectionService.subset(id);
        return ResponseEntity.ok(voList);
    }

    /**
     * 查询 fields.
     *
     * @param id th pk.
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections')")
    @GetMapping("/{id}/fields")
    public ResponseEntity<List<SectionFieldVO>> fields(@PathVariable Long id) {
        List<SectionFieldVO> voList = sectionService.fields(id);
        return ResponseEntity.ok(voList);
    }

    /**
     * create.
     *
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections:create')")
    @PostMapping
    public ResponseEntity<SectionVO> create(@Valid @RequestBody SectionDTO dto) {
        SectionVO vo = sectionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * modify.
     *
     * @param id  the pk.
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections:modify')")
    @PutMapping("/{id}")
    public ResponseEntity<SectionVO> modify(@PathVariable Long id, @RequestBody SectionDTO dto) {
        SectionVO vo = sectionService.modify(id, dto);
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * remove.
     *
     * @param id the pk.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        sectionService.remove(id);
        return ResponseEntity.ok().build();
    }

    /**
     * enable.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections:enable')")
    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = sectionService.enable(id);
        return ResponseEntity.ok(enabled);
    }

}
