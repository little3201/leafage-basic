/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.UserGroup;
import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.service.UserRoleService;
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
import top.leafage.common.basic.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserGroupService userGroupService;

    @MockBean
    private UserRoleService userRoleService;

    @MockBean
    private AuthorityService authorityService;

    @Test
    void retrieve() throws Exception {
        List<UserVO> voList = new ArrayList<>(2);
        UserVO userVO = new UserVO();
        userVO.setMobile("18710231023");
        voList.add(userVO);
        Page<UserVO> voPage = new PageImpl<>(voList);
        given(this.userService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

        mvc.perform(get("/user").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.userService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/user").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().is(204)).andDo(print()).andReturn();
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
        given(this.userService.details(Mockito.anyString())).willReturn(userDetails);

        mvc.perform(get("/user/{username}/details", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetchDetails_error() throws Exception {
        given(this.userService.details(Mockito.anyString())).willThrow(new RuntimeException());

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

    @Test
    void roles() throws Exception {
        given(this.userRoleService.roles(Mockito.anyString())).willReturn(Mockito.anyList());

        mvc.perform(get("/user/{username}/role", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void roles_error() throws Exception {
        given(this.userRoleService.roles(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/user/{username}/role", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_role() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(1L);
        given(this.userRoleService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Collections.singletonList(userRole));

        mvc.perform(patch("/user/{username}/role", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test")))).andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_role_error() throws Exception {
        given(this.userRoleService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        mvc.perform(patch("/user/{username}/role", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test")))).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void groups() throws Exception {
        given(this.userGroupService.groups(Mockito.anyString())).willReturn(Mockito.anyList());

        mvc.perform(get("/user/{username}/group", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void groups_error() throws Exception {
        given(this.userGroupService.groups(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/user/{username}/group", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_group() throws Exception {
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(1L);
        userGroup.setGroupId(1L);
        given(this.userGroupService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Collections.singletonList(userGroup));

        mvc.perform(patch("/user/{username}/group", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test")))).andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_group_error() throws Exception {
        given(this.userGroupService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        mvc.perform(patch("/user/{username}/group", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test")))).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void authority() throws Exception {
        TreeNode treeNode = new TreeNode("test", "test");
        given(this.authorityService.authorities(Mockito.anyString(), Mockito.anyChar()))
                .willReturn(Collections.singletonList(treeNode));

        mvc.perform(get("/user/{username}/authority", "test").param("type", "M"))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void authority_error() throws Exception {
        given(this.authorityService.authorities(Mockito.anyString(), Mockito.anyChar()))
                .willThrow(new RuntimeException());

        mvc.perform(get("/user/{username}/authority", "test").param("type", "M"))
                .andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

}