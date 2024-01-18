/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.domain.RolePrivileges;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.RoleMembersService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import top.leafage.common.TreeNode;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * role controller test
 *
 * @author liwenqiang 2019/9/14 21:52
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RoleService roleService;

    @MockBean
    private RoleMembersService roleMembersService;

    @MockBean
    private RolePrivilegesService rolePrivilegesService;

    private RoleVO roleVO;

    private RoleDTO roleDTO;

    @BeforeEach
    void init() {
        roleVO = new RoleVO();
        roleVO.setName("test");
        roleVO.setSuperior("superior");
        roleVO.setDescription("description");

        roleDTO = new RoleDTO();
        roleDTO.setName("test");
        roleDTO.setSuperiorId(1L);
        roleDTO.setAuthorities(Set.of("test"));
        roleDTO.setDescription("description");
    }

    @Test
    void retrieve() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        Page<RoleVO> voPage = new PageImpl<>(List.of(roleVO), pageable, 2L);
        given(this.roleService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

        mvc.perform(get("/roles").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.roleService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/roles").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        given(this.roleService.fetch(Mockito.anyLong())).willReturn(roleVO);

        mvc.perform(get("/roles/{id}", Mockito.anyLong())).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.roleService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/roles/{id}", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willReturn(roleVO);

        mvc.perform(post("/roles").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roleDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/roles").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roleDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(this.roleService.modify(Mockito.anyLong(), Mockito.any(RoleDTO.class))).willReturn(roleVO);

        mvc.perform(put("/roles/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roleDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.roleService.modify(Mockito.anyLong(), Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/roles/{id}", Mockito.anyLong()).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roleDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.roleService.remove(Mockito.anyLong());

        mvc.perform(delete("/roles/{id}", Mockito.anyLong()).with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.roleService).remove(Mockito.anyLong());

        mvc.perform(delete("/roles/{id}", Mockito.anyLong()).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void members() throws Exception {
        given(this.roleMembersService.members(Mockito.anyLong())).willReturn(Mockito.anyList());

        mvc.perform(get("/roles/{id}/members", 1L)).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void members_error() throws Exception {
        doThrow(new RuntimeException()).when(this.roleMembersService).members(Mockito.anyLong());

        mvc.perform(get("/roles/{id}/members", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void authorities() throws Exception {
        given(this.rolePrivilegesService.privileges(Mockito.anyLong())).willReturn(Mockito.anyList());

        mvc.perform(get("/roles/{id}/privileges", 1L)).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void authorities_error() throws Exception {
        doThrow(new RuntimeException()).when(this.rolePrivilegesService).privileges(Mockito.anyLong());

        mvc.perform(get("/roles/{id}/privileges", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void relation() throws Exception {
        given(this.rolePrivilegesService.relation(Mockito.anyLong(), Mockito.anySet()))
                .willReturn(List.of(Mockito.mock(RolePrivileges.class)));

        mvc.perform(patch("/roles/{id}/privileges", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Set.of(1L))).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_error() throws Exception {
        doThrow(new RuntimeException()).when(this.rolePrivilegesService).relation(Mockito.anyLong(), Mockito.anySet());

        mvc.perform(patch("/roles/{id}/privileges", Mockito.anyLong()).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Set.of(1L))).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }


    @Test
    void tree() throws Exception {
        TreeNode treeNode = new TreeNode(1L, "test");
        given(this.roleService.tree()).willReturn(Collections.singletonList(treeNode));

        mvc.perform(get("/roles/tree")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void tree_error() throws Exception {
        given(this.roleService.tree()).willThrow(new RuntimeException());

        mvc.perform(get("/roles/tree")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

}