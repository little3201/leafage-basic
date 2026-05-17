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
import top.leafage.common.poi.excel.ExcelReader;
import top.leafage.hypervisor.assets.domain.dto.ReportDTO;
import top.leafage.hypervisor.assets.domain.vo.ReportVO;
import top.leafage.hypervisor.assets.service.ReportService;

import java.io.IOException;
import java.util.List;

/**
 * report controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 分页查询
     *
     * @param page       页码
     * @param size       分页数据大小，最大 500
     * @param sortBy     排序字段，若为 null，默认为主键 id
     * @param descending 排序方向，若为 false, 即正序排列
     * @param filters    过滤条件，格式：field:condition:value，如：name:like:test
     * @return 查询的数据集，异常时返回204状态码
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_reports')")
    @GetMapping
    public ResponseEntity<Page<ReportVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                   String sortBy, boolean descending, String filters) {
        Page<ReportVO> voPage = reportService.retrieve(page, size, sortBy, descending, filters);
        return ResponseEntity.ok(voPage);
    }

    /**
     * fetch.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_reports')")
    @GetMapping("/{id}")
    public ResponseEntity<ReportVO> fetch(@PathVariable Long id) {
        ReportVO vo = reportService.fetch(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * create.
     *
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_reports:create')")
    @PostMapping
    public ResponseEntity<ReportVO> create(@Valid @RequestBody ReportDTO dto) {
        ReportVO vo = reportService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    /**
     * modify.
     *
     * @param id  the pk.
     * @param dto the request body.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_reports:modify')")
    @PutMapping("/{id}")
    public ResponseEntity<ReportVO> modify(@PathVariable Long id, @RequestBody ReportDTO dto) {
        ReportVO vo = reportService.modify(id, dto);
        return ResponseEntity.accepted().body(vo);
    }

    /**
     * Remove.
     *
     * @param id the pk.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_reports:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        reportService.remove(id);
        return ResponseEntity.ok().build();
    }

    /**
     * enable.
     *
     * @param id the pk.
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_reports:enable')")
    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> enable(@PathVariable Long id) {
        boolean enabled = reportService.enable(id);
        return ResponseEntity.ok(enabled);
    }

    /**
     * import.
     *
     * @return the result.
     */
    @PreAuthorize("hasRole('USER') || hasAuthority('SCOPE_reports:import')")
    @PostMapping("/import")
    public ResponseEntity<List<ReportVO>> importFromFile(MultipartFile file) throws IOException {
        List<ReportDTO> dtoList = ExcelReader.read(file.getInputStream(), ReportDTO.class);
        List<ReportVO> voList = reportService.createAll(dtoList);
        return ResponseEntity.ok().body(voList);
    }

}
