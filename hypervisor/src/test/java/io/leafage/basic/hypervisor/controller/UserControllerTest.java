/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private UserVO userVO;

    private UserDTO userDTO;

    @BeforeEach
    void init() {
        userVO = new UserVO();
        userVO.setFirstname("test");

        userDTO = new UserDTO();
        userDTO.setUsername("test");
    }

    @Test
    void fetch() throws Exception {
        given(this.userService.fetch(Mockito.anyLong())).willReturn(userVO);

        mvc.perform(get("/users/{username}", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.userService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/users/{username}", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(this.userService.modify(Mockito.anyLong(), Mockito.any(UserDTO.class))).willReturn(userVO);

        mvc.perform(put("/users/{username}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.userService.modify(Mockito.anyLong(), Mockito.any(UserDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/users/{username}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

}