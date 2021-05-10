/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;


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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockBean
    private RoleService roleService;

    @Test
    void retrieve() throws Exception {
        List<RoleVO> voList = new ArrayList<>(2);
        voList.add(new RoleVO());
        Page<RoleVO> userVOPage = new PageImpl<>(voList);
        given(this.roleService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(userVOPage);
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


}