/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
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
 * 用户测试
 *
 * @author liwenqiang 2019/1/29 17:09
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void retrieve() throws Exception {
        List<UserVO> voList = new ArrayList<>(2);
        voList.add(new UserVO());
        Page<UserVO> userVOPage = new PageImpl<>(voList);
        given(this.userService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(userVOPage);
        mvc.perform(get("/user").queryParam("page", "0").queryParam("size", "2")
                .queryParam("order", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.userService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());
        mvc.perform(get("/user").queryParam("page", "0").queryParam("size", "2")
                .queryParam("order", "")).andExpect(status().is(204)).andDo(print()).andReturn();
    }

}