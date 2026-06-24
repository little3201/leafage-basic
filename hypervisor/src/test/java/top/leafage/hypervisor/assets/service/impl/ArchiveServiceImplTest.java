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

package top.leafage.hypervisor.assets.service.impl;

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
import top.leafage.hypervisor.assets.domain.Archive;
import top.leafage.hypervisor.assets.domain.vo.ArchiveVO;
import top.leafage.hypervisor.assets.repository.ArchiveRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * archive service test
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
class ArchiveServiceImplTest {

    @Mock
    private ArchiveRepository archiveRepository;

    @InjectMocks
    private ArchiveServiceImpl archiveService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void retrieve() {
        Page<Archive> page = new PageImpl<>(List.of(mock(Archive.class)));

        when(archiveRepository.findAll(ArgumentMatchers.<Specification<Archive>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<ArchiveVO> voPage = archiveService.retrieve(0, 2, "id", true, "title:like:test");
        assertEquals(1, voPage.getTotalElements());
        assertEquals(1, voPage.getContent().size());
        verify(archiveRepository).findAll(ArgumentMatchers.<Specification<Archive>>any(), any(Pageable.class));
    }

    @Test
    void fetch() {
    }

    @Test
    void create() {
    }

    @Test
    void modify() {
    }

    @Test
    void remove() {
    }
}