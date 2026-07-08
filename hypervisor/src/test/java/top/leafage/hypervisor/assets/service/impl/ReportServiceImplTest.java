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

package top.leafage.hypervisor.assets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import top.leafage.hypervisor.assets.domain.Report;
import top.leafage.hypervisor.assets.domain.dto.ReportDTO;
import top.leafage.hypervisor.assets.domain.vo.ReportVO;
import top.leafage.hypervisor.assets.repository.ReportRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

/**
 * Report service test
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private ReportDTO dto;
    private Report entity;

    @BeforeEach
    void setUp() {
        dto = new ReportDTO();
        dto.setTitle("test");
        dto.setBody("body");
        dto.setOwner("owner");

        entity = ReportDTO.toEntity(dto);
    }

    @Test
    void retrieve() {
        Page<Report> page = new PageImpl<>(List.of(entity));
        when(reportRepository.findAll(ArgumentMatchers.<Specification<Report>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<ReportVO> voPage = reportService.retrieve(0, 2, "id", true, "title:like:test");
        assertEquals(1, voPage.getTotalElements());
        verify(reportRepository).findAll(ArgumentMatchers.<Specification<Report>>any(), any(Pageable.class));
    }

    @Test
    void fetch() {
        when(reportRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        ReportVO vo = reportService.fetch(anyLong());
        assertNotNull(vo);
        assertEquals("test", vo.title());
    }

    @Test
    void create() {
        when(reportRepository.existsByTitle("test")).thenReturn(false);
        when(reportRepository.save(any(Report.class))).thenReturn(entity);

        ReportVO vo = reportService.create(dto);
        assertNotNull(vo);
        assertEquals("test", vo.title());
    }

    @Test
    void modify() {
        when(reportRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(reportRepository.existsByTitle("demo")).thenReturn(false);
        when(reportRepository.save(any(Report.class))).thenReturn(entity);

        dto.setTitle("demo");
        ReportVO vo = reportService.modify(1L, dto);
        assertNotNull(vo);
        assertEquals("demo", vo.title());
    }

    @Test
    void remove_not_found() {
        when(reportRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> reportService.remove(anyLong())
        );
        assertEquals("report not found: 0", exception.getMessage());
    }

    @Test
    void preview() {
        assertEquals("", reportService.preview(1L));
    }
}
