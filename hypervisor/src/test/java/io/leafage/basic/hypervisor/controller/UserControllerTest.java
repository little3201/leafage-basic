/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * user controller test
 *
 * @author liwenqiang 2019/1/29 17:09
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Test
    void fetch() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setFirstname("test");
        given(this.userService.fetch(Mockito.anyString())).willReturn(userVO);

        mvc.perform(get("/user/{username}", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.userService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/user/{username}", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        UserVO userVO = new UserVO();
        userVO.setFirstname("test");
        given(this.userService.create(Mockito.any(UserDTO.class))).willReturn(userVO);

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.userService.create(Mockito.any(UserDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        UserVO userVO = new UserVO();
        userVO.setFirstname("test");
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willReturn(userVO);

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(put("/user/{username}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(put("/user/{username}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.userService.remove(Mockito.anyString());

        mvc.perform(delete("/user/{username}", "test").with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.userService).remove(Mockito.anyString());

        mvc.perform(delete("/user/{username}", "test").with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

}