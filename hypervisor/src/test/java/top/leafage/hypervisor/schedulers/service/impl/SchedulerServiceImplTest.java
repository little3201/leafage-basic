/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.schedulers.service.impl;

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
import org.springframework.test.util.ReflectionTestUtils;
import top.leafage.hypervisor.schedulers.domain.Scheduler;
import top.leafage.hypervisor.schedulers.domain.dto.SchedulerDTO;
import top.leafage.hypervisor.schedulers.domain.vo.SchedulerVO;
import top.leafage.hypervisor.schedulers.repository.SchedulerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceImplTest {

    @Mock
    private SchedulerRepository schedulerRepository;

    @InjectMocks
    private SchedulerServiceImpl schedulerService;

    private Scheduler scheduler;
    private SchedulerDTO dto;

    @BeforeEach
    void setUp() {
        scheduler = new Scheduler("clear logs", "0 0 0 * * ?");
        scheduler.setStatus(Scheduler.Status.PENDING);
        scheduler.setRecord("waiting");
        ReflectionTestUtils.setField(scheduler, "id", 1L);

        dto = new SchedulerDTO();
        dto.setName("clear logs");
        dto.setCronExpression("0 0 0 * * ?");
    }

    @Test
    void retrieve() {
        Page<Scheduler> page = new PageImpl<>(List.of(scheduler));
        when(schedulerRepository.findAll(ArgumentMatchers.<Specification<Scheduler>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<SchedulerVO> voPage = schedulerService.retrieve(0, 2, "id", true, "name:like:clear");

        assertEquals(1, voPage.getTotalElements());
        assertEquals("clear logs", voPage.getContent().getFirst().name());
        verify(schedulerRepository).findAll(ArgumentMatchers.<Specification<Scheduler>>any(), any(Pageable.class));
    }

    @Test
    void fetch() {
        when(schedulerRepository.findById(anyLong())).thenReturn(Optional.of(scheduler));

        SchedulerVO vo = schedulerService.fetch(1L);

        assertEquals(1L, vo.id());
        assertEquals("0 0 0 * * ?", vo.cronExpression());
    }

    @Test
    void fetch_not_found() {
        when(schedulerRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> schedulerService.fetch(1L));
        assertEquals("scheduler log not found: 1", exception.getMessage());
    }

    @Test
    void enable() {
        when(schedulerRepository.existsById(anyLong())).thenReturn(true);
        when(schedulerRepository.enableById(anyLong())).thenReturn(1);

        assertTrue(schedulerService.enable(1L));
        verify(schedulerRepository).enableById(1L);
    }

    @Test
    void enable_not_found() {
        when(schedulerRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> schedulerService.enable(1L));
        assertEquals("scheduler not found: 1", exception.getMessage());
    }

    @Test
    void disable() {
        when(schedulerRepository.existsById(anyLong())).thenReturn(true);
        when(schedulerRepository.enableById(anyLong())).thenReturn(1);

        assertTrue(schedulerService.disable(1L));
        verify(schedulerRepository).enableById(1L);
    }

    @Test
    void disable_not_found() {
        when(schedulerRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> schedulerService.disable(1L));
        assertEquals("scheduler not found: 1", exception.getMessage());
    }

    @Test
    void create() {
        when(schedulerRepository.existsByName(anyString())).thenReturn(false);
        when(schedulerRepository.save(any(Scheduler.class))).thenReturn(scheduler);

        SchedulerVO vo = schedulerService.create(dto);

        assertEquals("clear logs", vo.name());
        verify(schedulerRepository).save(any(Scheduler.class));
    }

    @Test
    void create_duplicate_name() {
        when(schedulerRepository.existsByName(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> schedulerService.create(dto));
        assertEquals("name already exists: clear logs", exception.getMessage());
    }

    @Test
    void modify() {
        dto.setCronExpression("0 0 1 * * ?");
        when(schedulerRepository.findById(anyLong())).thenReturn(Optional.of(scheduler));
        when(schedulerRepository.save(any(Scheduler.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SchedulerVO vo = schedulerService.modify(1L, dto);

        assertEquals("0 0 1 * * ?", vo.cronExpression());
        verify(schedulerRepository).save(scheduler);
    }

    @Test
    void modify_duplicate_name() {
        dto.setName("archive logs");
        when(schedulerRepository.findById(anyLong())).thenReturn(Optional.of(scheduler));
        when(schedulerRepository.existsByName("archive logs")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> schedulerService.modify(1L, dto));
        assertEquals("name already exists: archive logs", exception.getMessage());
    }

    @Test
    void remove() {
        when(schedulerRepository.existsById(anyLong())).thenReturn(true);

        schedulerService.remove(1L);

        verify(schedulerRepository).deleteById(1L);
    }

    @Test
    void remove_not_found() {
        when(schedulerRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> schedulerService.remove(1L));
        assertEquals("scheduler log not found: 1", exception.getMessage());
    }
}
