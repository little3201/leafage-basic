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

package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import top.leafage.common.TreeNode;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * privilege controller test
 *
 * @author wq li
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(PrivilegeController.class)
class PrivilegeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PrivilegeService privilegeService;

    @MockBean
    private RolePrivilegesService rolePrivilegesService;

    @Test
    void tree() throws Exception {
        TreeNode treeNode = TreeNode.withId(1L).name("test").build();
        given(this.privilegeService.tree(Mockito.anyString())).willReturn(Collections.singletonList(treeNode));

        mvc.perform(get("/privileges"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void tree_error() throws Exception {
        given(this.privilegeService.tree(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/privileges"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
    }

    @Test
    void roles() throws Exception {
        given(this.rolePrivilegesService.roles(Mockito.anyLong())).willReturn(Mockito.anyList());

        mvc.perform(get("/privileges/{id}/roles", 1L)).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void roles_error() throws Exception {
        given(this.rolePrivilegesService.roles(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/privileges/{id}/roles", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }
}