/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
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
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void retrieve() throws Exception {
        List<UserVO> voList = new ArrayList<>(2);
        voList.add(new UserVO());
        Page<UserVO> voPage = new PageImpl<>(voList);
        given(this.userService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

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

    @Test
    void fetch() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setUsername("test");
        given(this.userService.fetch(Mockito.anyString())).willReturn(userVO);

        mvc.perform(get("/user/{username}", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.userService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/user/{username}", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void fetchDetails() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername("test");
        given(this.userService.fetchDetails(Mockito.anyString())).willReturn(userDetails);

        mvc.perform(get("/user/{username}/details", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetchDetails_error() throws Exception {
        given(this.userService.fetchDetails(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/user/{username}/details", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        UserVO userVO = new UserVO();
        userVO.setUsername("test");
        given(this.userService.create(Mockito.any(UserDTO.class))).willReturn(userVO);

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDTO))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.userService.create(Mockito.any(UserDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDTO))).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        UserVO postsVO = new UserVO();
        postsVO.setUsername("test");
        given(this.userService.modify(Mockito.anyString(), Mockito.any(UserDTO.class))).willReturn(postsVO);

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(put("/user/{username}", "test").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDTO)))
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
                .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.userService.remove(Mockito.anyString());

        mvc.perform(delete("/user/{username}", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.userService).remove(Mockito.anyString());

        mvc.perform(delete("/user/{username}", "test")).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

}