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
import top.leafage.common.data.domain.TreeNode;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.dto.SectionFieldDTO;
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
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections')")
    @GetMapping("/{id}")
    public ResponseEntity<SectionVO> fetch(@PathVariable Long id) {
        SectionVO vo = sectionService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * tree.
     *
     * @param ownerId the pk.
     * @param ownerType tye type.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections')")
    @GetMapping("/{ownerId}/tree")
    public ResponseEntity<List<TreeNode<Long>>> tree(@PathVariable Long ownerId, @RequestParam String ownerType) {
        List<TreeNode<Long>> treeNodes = sectionService.tree(ownerId, ownerType);
        return ResponseEntity.ok(treeNodes);
    }

    /**
     * 查询 fields.
     *
     * @param id the pk.
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
     * Create field.
     *
     * @param dto the request body.
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections')")
    @PostMapping("/fields")
    public ResponseEntity<SectionFieldVO> createField(@RequestBody SectionFieldDTO dto) {
        SectionFieldVO vo = sectionService.createField(dto);
        return ResponseEntity.ok(vo);
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
     * Modify field.
     *
     * @param id  the pk of field.
     * @param dto the request body.
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_sections')")
    @PutMapping("/fields/{id}")
    public ResponseEntity<SectionFieldVO> modifyField(@PathVariable Long id, @RequestBody SectionFieldDTO dto) {
        SectionFieldVO vo = sectionService.modifyField(id, dto);
        return ResponseEntity.ok(vo);
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
