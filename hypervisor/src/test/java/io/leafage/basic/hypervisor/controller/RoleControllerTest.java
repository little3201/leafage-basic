/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.service.RoleService;
import io.leafage.basic.hypervisor.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 角色接口测试类
 *
 * @author liwenqiang 2019/9/14 21:52
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RoleService roleService;

    @Test
    void retrieve() throws Exception {
        List<RoleVO> voList = new ArrayList<>(2);
        voList.add(new RoleVO());
        Page<RoleVO> voPage = new PageImpl<>(voList);
        given(this.roleService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

        mvc.perform(get("/role").queryParam("page", "0").queryParam("size", "2")
                .queryParam("order", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.roleService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/role").queryParam("page", "0").queryParam("size", "2")
                .queryParam("order", "")).andExpect(status().is(204)).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.fetch(Mockito.anyString())).willReturn(roleVO);

        mvc.perform(get("/role/{code}", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.roleService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/role/{code}", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willReturn(roleVO);

        // 构造请求对象
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        mvc.perform(post("/role").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(roleDTO))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.roleService.create(Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        mvc.perform(post("/role").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(roleDTO))).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        RoleVO roleVO = new RoleVO();
        roleVO.setName("test");
        given(this.roleService.modify(Mockito.anyString(), Mockito.any(RoleDTO.class))).willReturn(roleVO);

        // 构造请求对象
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        mvc.perform(put("/role/{code}", "test").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(roleDTO)))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.roleService.modify(Mockito.anyString(), Mockito.any(RoleDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("test");
        mvc.perform(put("/role/{code}", "test").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(roleDTO)))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.roleService.remove(Mockito.anyString());

        mvc.perform(delete("/role/{code}", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.roleService).remove(Mockito.anyString());

        mvc.perform(delete("/role/{code}", "test")).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }


}