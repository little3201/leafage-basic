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

import io.leafage.basic.hypervisor.domain.Privilege;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.repository.PrivilegeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import top.leafage.common.TreeNode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * privilege service test
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
class PrivilegeServiceImplTest {

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private PrivilegeServiceImpl privilegeService;

    private PrivilegeDTO privilegeDTO;

    @BeforeEach
    void setUp() {
        privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setName("system");
        privilegeDTO.setPath("/system");
        privilegeDTO.setRedirect("/system/user");
        privilegeDTO.setComponent("#");
        privilegeDTO.setIcon("user");
        privilegeDTO.setSuperiorId(1L);
    }

    @Test
    void tree() {
        given(this.privilegeRepository.findAll()).willReturn(Arrays.asList(Mockito.mock(Privilege.class), Mockito.mock(Privilege.class)));

        List<TreeNode> nodes = privilegeService.tree();
        Assertions.assertNotNull(nodes);
    }

}