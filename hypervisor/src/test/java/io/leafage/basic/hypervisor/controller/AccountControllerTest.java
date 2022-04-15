/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.AccountDTO;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.AccountGroup;
import io.leafage.basic.hypervisor.entity.AccountRole;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.service.AccountRoleService;
import io.leafage.basic.hypervisor.service.AccountService;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import top.leafage.common.basic.TreeNode;
import java.util.Collections;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * account controller test
 *
 * @author liwenqiang 2022/1/26 15:37
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountGroupService accountGroupService;

    @MockBean
    private AccountRoleService accountRoleService;

    @MockBean
    private AuthorityService authorityService;

    @Test
    void retrieve() throws Exception {
        AccountVO accountVO = new AccountVO();
        accountVO.setNickname("布吉岛");
        Page<AccountVO> voPage = new PageImpl<>(List.of(accountVO));
        given(this.accountService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(voPage);

        mvc.perform(get("/account").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.accountService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        mvc.perform(get("/account").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        AccountVO accountVO = new AccountVO();
        accountVO.setNickname("test");
        given(this.accountService.fetch(Mockito.anyString())).willReturn(accountVO);

        mvc.perform(get("/account/{username}", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.accountService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/account/{username}", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void exist() throws Exception {
        given(this.accountService.exist(Mockito.anyString())).willReturn(true);

        mvc.perform(get("/account/{username}/exist", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void exist_error() throws Exception {
        given(this.accountService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/account/{username}/exist", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void unlock() throws Exception {
        AccountVO accountVO = new AccountVO();
        accountVO.setUsername("test");
        accountVO.setAccountLocked(false);
        given(this.accountService.unlock(Mockito.anyString())).willReturn(accountVO);

        mvc.perform(patch("/account/{username}", "test").with(csrf().asHeader())).andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void unlock_error() throws Exception {
        given(this.accountService.unlock(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(patch("/account/{username}", "test").with(csrf().asHeader())).andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        AccountVO accountVO = new AccountVO();
        accountVO.setNickname("test");
        given(this.accountService.create(Mockito.any(AccountDTO.class))).willReturn(accountVO);

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(post("/account").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.accountService.create(Mockito.any(AccountDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(post("/account").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        AccountVO accountVO = new AccountVO();
        accountVO.setNickname("test");
        given(this.accountService.modify(Mockito.anyString(), Mockito.any(AccountDTO.class))).willReturn(accountVO);

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(put("/account/{username}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.accountService.modify(Mockito.anyString(), Mockito.any(AccountDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        mvc.perform(put("/account/{username}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.accountService.remove(Mockito.anyString());

        mvc.perform(delete("/account/{username}", "test").with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.accountService).remove(Mockito.anyString());

        mvc.perform(delete("/account/{username}", "test").with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void roles() throws Exception {
        given(this.accountRoleService.roles(Mockito.anyString())).willReturn(Mockito.anyList());

        mvc.perform(get("/account/{username}/role", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void roles_error() throws Exception {
        given(this.accountRoleService.roles(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/account/{username}/role", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_role() throws Exception {
        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(1L);
        accountRole.setRoleId(1L);
        given(this.accountRoleService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Collections.singletonList(accountRole));

        mvc.perform(patch("/account/{username}/role", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test"))).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_role_error() throws Exception {
        given(this.accountRoleService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        mvc.perform(patch("/account/{username}/role", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test"))).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void groups() throws Exception {
        given(this.accountGroupService.groups(Mockito.anyString())).willReturn(Mockito.anyList());

        mvc.perform(get("/account/{username}/group", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void groups_error() throws Exception {
        given(this.accountGroupService.groups(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/account/{username}/group", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_group() throws Exception {
        AccountGroup accountGroup = new AccountGroup();
        accountGroup.setAccountId(1L);
        accountGroup.setGroupId(1L);
        given(this.accountGroupService.relation(Mockito.anyString(), Mockito.anySet()))
                .willReturn(Collections.singletonList(accountGroup));

        mvc.perform(patch("/account/{username}/group", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test"))).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void relation_group_error() throws Exception {
        given(this.accountGroupService.relation(Mockito.anyString(), Mockito.anySet())).willThrow(new RuntimeException());

        mvc.perform(patch("/account/{username}/group", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Collections.singleton("test"))).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void authority() throws Exception {
        TreeNode treeNode = new TreeNode("test", "test");
        given(this.authorityService.authorities(Mockito.anyString(), Mockito.anyChar()))
                .willReturn(Collections.singletonList(treeNode));

        mvc.perform(get("/account/{username}/authority", "test").param("type", "M"))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void authority_error() throws Exception {
        given(this.authorityService.authorities(Mockito.anyString(), Mockito.anyChar()))
                .willThrow(new RuntimeException());

        mvc.perform(get("/account/{username}/authority", "test").param("type", "M"))
                .andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

}