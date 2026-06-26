/*
 * Copyright (c) 2024-2026.  little3201.
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

package top.leafage.hypervisor.system.service.impl;

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
import top.leafage.common.data.core.domain.TreeNode;
import top.leafage.hypervisor.system.domain.Group;
import top.leafage.hypervisor.system.domain.dto.GroupDTO;
import top.leafage.hypervisor.system.domain.vo.GroupVO;
import top.leafage.hypervisor.system.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


/**
 * group service test
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    private GroupDTO dto;
    @Mock
    private Group entity;

    @BeforeEach
    void setUp() {
        dto = new GroupDTO();
        dto.setName("test");
        dto.setSuperiorId(1L);

        entity = new Group("test", null);
    }

    @Test
    void retrieve() {
        Page<Group> page = new PageImpl<>(List.of(entity));

        when(groupRepository.findAll(ArgumentMatchers.<Specification<Group>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<GroupVO> voPage = groupService.retrieve(0, 2, "id", true, "name:like:test");
        assertEquals(1, voPage.getTotalElements());
        assertEquals(1, voPage.getContent().size());
        verify(groupRepository).findAll(ArgumentMatchers.<Specification<Group>>any(), any(Pageable.class));
    }

    @Test
    void tree() {
        ReflectionTestUtils.setField(entity, "id", 1L);
        Group child = new Group("test", 1L);
        ReflectionTestUtils.setField(child, "id", 2L);

        when(groupRepository.findAll()).thenReturn(List.of(entity, child));

        List<TreeNode<Long>> nodes = groupService.tree();
        assertEquals(1, nodes.size());
        assertEquals(1, nodes.getFirst().getChildren().size());
        verify(groupRepository).findAll();
    }

    @Test
    void fetch() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        GroupVO vo = groupService.fetch(anyLong());
        assertNotNull(vo);
        assertEquals("test", vo.name());
        verify(groupRepository).findById(anyLong());
    }

    @Test
    void fetch_not_found() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> groupService.fetch(anyLong())
        );
        assertEquals("group not found: 0", exception.getMessage());
        verify(groupRepository).findById(anyLong());
    }

    @Test
    void create() {
        when(groupRepository.existsByName("test")).thenReturn(false);
        when(groupRepository.save(any(Group.class))).thenReturn(entity);

        GroupVO vo = groupService.create(dto);
        assertNotNull(vo);
        assertEquals("test", vo.name());
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void create_name_conflict() {
        when(groupRepository.existsByName("test")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> groupService.create(dto)
        );
        assertEquals("name already exists: test", exception.getMessage());
        verify(groupRepository, never()).save(any());
    }

    @Test
    void modify() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(groupRepository.existsByName("demo")).thenReturn(false);
        when(groupRepository.save(any(Group.class))).thenReturn(entity);

        dto.setName("demo");
        GroupVO vo = groupService.modify(1L, dto);
        assertNotNull(vo);
        assertEquals("demo", vo.name());
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void modify_username_conflict() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(groupRepository.existsByName("demo")).thenReturn(true);

        dto.setName("demo");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> groupService.modify(1L, dto)
        );
        assertEquals("name already exists: demo", exception.getMessage());
    }

    @Test
    void remove() {
        when(groupRepository.existsById(anyLong())).thenReturn(true);
        groupService.remove(1L);

        verify(groupRepository).deleteById(anyLong());
    }

    @Test
    void remove_not_found() {
        when(groupRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> groupService.remove(anyLong())
        );
        assertEquals("group not found: 0", exception.getMessage());
    }

    @Test
    void enable() {
        when(groupRepository.existsById(anyLong())).thenReturn(true);
        when(groupRepository.enableById(anyLong())).thenReturn(1);

        boolean enabled = groupService.enable(1L);
        assertTrue(enabled);
    }

    @Test
    void enable_not_found() {
        when(groupRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> groupService.enable(1L)
        );
        assertEquals("group not found: 1", exception.getMessage());
    }
}