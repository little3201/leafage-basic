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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.leafage.common.data.domain.TreeNode;
import top.leafage.common.poi.excel.ExcelReader;
import top.leafage.hypervisor.assets.domain.dto.SchemaDTO;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.vo.SchemaVO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.service.SchemaService;
import top.leafage.hypervisor.assets.service.SectionService;

import java.io.IOException;
import java.util.List;

/**
 * schema controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/schemas")
public class SchemaController {

    private final SchemaService schemaService;
    private final SectionService sectionService;

    public SchemaController(SchemaService schemaService, SectionService sectionService) {
        this.schemaService = schemaService;
        this.sectionService = sectionService;
    }

    /**
     * 分页查询
     *
     * @param page       页码
     * @param size       大小
     * @param sortBy     排序字段
     * @param descending 排序方向
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas')")
    @GetMapping
    public ResponseEntity<Page<SchemaVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                   String sortBy, boolean descending, String filters) {
        Page<SchemaVO> voPage = schemaService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * fetch.
     *
     * @param id th pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas')")
    @GetMapping("/{id}")
    public ResponseEntity<SchemaVO> fetch(@PathVariable Long id) {
        SchemaVO vo = schemaService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * create.
     *
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas:create')")
    @PostMapping
    public ResponseEntity<SchemaVO> create(@Valid @RequestBody SchemaDTO dto) {
        SchemaVO vo = schemaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * modify.
     *
     * @param id  the pk.
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas:modify')")
    @PutMapping("/{id}")
    public ResponseEntity<SchemaVO> modify(@PathVariable Long id, @RequestBody SchemaDTO dto) {
        SchemaVO vo = schemaService.modify(id, dto);
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * remove.
     *
     * @param id the pk.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        schemaService.remove(id);
        return ResponseEntity.ok().build();
    }

    /**
     * enable.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas:enable')")
    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = schemaService.enable(id);
        return ResponseEntity.ok(enabled);
    }

    /**
     * sections.
     *
     * @param id the pk.
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas')")
    @GetMapping("/{id}/sections")
    public ResponseEntity<List<TreeNode<Long>>> sections(@PathVariable Long id) {
        List<TreeNode<Long>> treeNodes = sectionService.schemaTree(id);
        return ResponseEntity.ok(treeNodes);
    }

    /**
     * Fetch section.
     *
     * @param sectionId the pk of section.
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas')")
    @GetMapping("/sections/{sectionId}")
    public ResponseEntity<SectionVO> fetchSection(@PathVariable Long sectionId) {
        SectionVO vo = sectionService.fetch(sectionId);
        return ResponseEntity.ok(vo);
    }

    /**
     * Create section.
     *
     * @param id  the pk.
     * @param dto the data of section.
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas')")
    @PostMapping("/{id}/sections")
    public ResponseEntity<SectionVO> createSection(@PathVariable Long id, @RequestBody SectionDTO dto) {
        SectionVO vo = sectionService.createSchemaSection(id, dto);
        return ResponseEntity.ok(vo);
    }

    /**
     * import.
     *
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_schemas:import')")
    @PostMapping("/import")
    public ResponseEntity<List<SchemaVO>> importFromFile(MultipartFile file) throws IOException {
        List<SchemaDTO> dtoList = ExcelReader.read(file.getInputStream(), SchemaDTO.class);
        List<SchemaVO> voList = schemaService.createAll(dtoList);
        return ResponseEntity.ok().body(voList);
    }

}
