/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Role;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * role service test
 *
 * @author wq li 2021/5/11 10:10
 **/
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleDTO roleDTO;

    @BeforeEach
    void init() {
        roleDTO = new RoleDTO();
        roleDTO.setName("role");
        roleDTO.setDescription("role");
    }

    @Test
    void retrieve() {
        Page<Role> page = new PageImpl<>(List.of(Mockito.mock(Role.class)));
        given(this.roleRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(page);

        Page<RoleVO> voPage = roleService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Role.class)));

        RoleVO roleVO = roleService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(roleVO);
    }

    @Test
    void create() {
        given(this.roleRepository.saveAndFlush(Mockito.any(Role.class))).willReturn(Mockito.mock(Role.class));

        RoleVO roleVO = roleService.create(Mockito.mock(RoleDTO.class));

        verify(this.roleRepository, times(1)).saveAndFlush(Mockito.any(Role.class));
        Assertions.assertNotNull(roleVO);
    }

    @Test
    void modify() {
        given(this.roleRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Role.class)));

        given(this.roleRepository.save(Mockito.any(Role.class))).willReturn(Mockito.mock(Role.class));

        RoleVO roleVO = roleService.modify(1L, roleDTO);

        verify(this.roleRepository, times(1)).save(Mockito.any(Role.class));
        Assertions.assertNotNull(roleVO);
    }

    @Test
    void remove() {
        roleService.remove(Mockito.anyLong());

        verify(this.roleRepository, times(1)).deleteById(Mockito.anyLong());
    }

}