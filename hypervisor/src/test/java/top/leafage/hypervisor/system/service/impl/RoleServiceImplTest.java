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
import top.leafage.hypervisor.system.domain.*;
import top.leafage.hypervisor.system.domain.dto.PrivilegeActionsDTO;
import top.leafage.hypervisor.system.domain.dto.RoleDTO;
import top.leafage.hypervisor.system.domain.vo.PrivilegeActionsVO;
import top.leafage.hypervisor.system.domain.vo.RoleVO;
import top.leafage.hypervisor.system.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Role service test
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePrivilegeRepository rolePrivilegeRepository;

    @Mock
    private PrivilegeRepository privilegeRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleDTO dto;
    private Role entity;

    @BeforeEach
    void setUp() {
        dto = new RoleDTO();
        dto.setName("test");
        dto.setCode("TEST");

        entity = RoleDTO.toEntity(dto);
    }

    @Test
    void retrieve() {
        Page<Role> page = new PageImpl<>(List.of(entity));

        when(roleRepository.findAll(ArgumentMatchers.<Specification<Role>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<RoleVO> voPage = roleService.retrieve(0, 2, "id", true, "test");
        assertEquals(1, voPage.getTotalElements());
        assertEquals(1, voPage.getContent().size());
        verify(roleRepository).findAll(ArgumentMatchers.<Specification<Role>>any(), any(Pageable.class));
    }

    @Test
    void fetch() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        RoleVO vo = roleService.fetch(anyLong());
        assertNotNull(vo);
        assertEquals("test", vo.name());
        verify(roleRepository).findById(anyLong());
    }

    @Test
    void fetch_not_found() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.fetch(anyLong())
        );
        assertEquals("role not found: 0", exception.getMessage());
        verify(roleRepository).findById(anyLong());
    }

    @Test
    void create() {
        when(roleRepository.existsByName("test")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(entity);

        RoleVO vo = roleService.create(dto);
        assertNotNull(vo);
        assertEquals("test", vo.name());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void create_username_conflict() {
        when(roleRepository.existsByName("test")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.create(dto)
        );
        assertEquals("name already exists: test", exception.getMessage());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void modify() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(roleRepository.existsByName("demo")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(entity);

        dto.setName("demo");
        RoleVO vo = roleService.modify(1L, dto);
        assertNotNull(vo);
        assertEquals("demo", vo.name());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void modify_username_conflict() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(roleRepository.existsByName("demo")).thenReturn(true);

        dto.setName("demo");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.modify(1L, dto)
        );
        assertEquals("name already exists: demo", exception.getMessage());
    }

    @Test
    void remove() {
        when(roleRepository.existsById(anyLong())).thenReturn(true);

        roleService.remove(anyLong());
        verify(roleRepository).deleteById(anyLong());
    }

    @Test
    void remove_not_found() {
        when(roleRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.remove(anyLong())
        );
        assertEquals("role not found: 0", exception.getMessage());
    }

    @Test
    void enable() {
        when(roleRepository.existsById(anyLong())).thenReturn(true);
        when(roleRepository.enableById(anyLong())).thenReturn(1);

        boolean enabled = roleService.enable(1L);
        assertTrue(enabled);
    }

    @Test
    void enable_not_found() {
        when(roleRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.enable(1L)
        );
        assertEquals("role not found: 1", exception.getMessage());
    }

    @Test
    void disable() {
        when(roleRepository.existsById(anyLong())).thenReturn(true);
        when(roleRepository.disableById(anyLong())).thenReturn(1);

        boolean disabled = roleService.disable(1L);
        assertTrue(disabled);
    }

    @Test
    void disable_not_found() {
        when(roleRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.disable(1L)
        );
        assertEquals("role not found: 1", exception.getMessage());
    }

    @Test
    void authorize() {
        ReflectionTestUtils.setField(entity, "id", 1L);
        Privilege privilege = new Privilege("roles", null, "/roles", null, null, Set.of("read", "modify"));
        ReflectionTestUtils.setField(privilege, "id", 2L);
        PrivilegeActionsDTO actionsDTO = new PrivilegeActionsDTO();
        actionsDTO.setPrivilegeId(2L);
        actionsDTO.setActions(Set.of("read"));

        when(roleRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(privilegeRepository.findAllById(Set.of(2L))).thenReturn(List.of(privilege));
        when(rolePrivilegeRepository.findAllByRoleId(1L)).thenReturn(List.of());
        when(groupRepository.findDisctinctByRolesContaining(entity)).thenReturn(List.of());
        when(userRepository.findDisctinctByRolesContaining(entity)).thenReturn(List.of());

        roleService.authorize(1L, List.of(actionsDTO));

        assertEquals(1, entity.getRolePrivileges().size());
        verify(groupRepository).saveAll(List.of());
        verify(userRepository).saveAll(List.of());
    }

    @Test
    void privileges() {
        Privilege privilege = new Privilege("roles", null, "/roles", null, null, Set.of("read"));
        ReflectionTestUtils.setField(privilege, "id", 2L);
        RolePrivilege rolePrivilege = new RolePrivilege(entity, privilege, Set.of("read"));
        when(rolePrivilegeRepository.findAllByRoleId(1L)).thenReturn(List.of(rolePrivilege));

        List<PrivilegeActionsVO> privileges = roleService.privileges(1L);

        assertEquals(1, privileges.size());
        assertEquals(2L, privileges.getFirst().privilegeId());
    }
}
