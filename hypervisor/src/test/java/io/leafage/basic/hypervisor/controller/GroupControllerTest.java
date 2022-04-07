package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.service.AccountGroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
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
 * group controller test
 *
 * @author liwenqiang 2019/9/14 21:52
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GroupService groupService;

    @MockBean
    private AccountGroupService accountGroupService;

    @Test
    void retrieve() throws Exception {
        GroupVO groupVO = new GroupVO();
        groupVO.setAlias("IT");
        groupVO.setPrincipal("admin");
        groupVO.setSuperior("superior");
        groupVO.setCount(2L);
        Page<GroupVO> voPage = new PageImpl<>(List.of(groupVO));
        given(this.groupService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

        mvc.perform(get("/group").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.groupService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/group").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.fetch(Mockito.anyString())).willReturn(groupVO);

        mvc.perform(get("/group/{code}", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.groupService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/group/{code}", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willReturn(groupVO);

        // 构造请求对象
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setDescription("描述");
        mvc.perform(post("/group").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        mvc.perform(post("/group").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        GroupVO groupVO = new GroupVO();
        groupVO.setName("test");
        given(this.groupService.modify(Mockito.anyString(), Mockito.any(GroupDTO.class))).willReturn(groupVO);

        // 构造请求对象
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        mvc.perform(put("/group/{code}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.groupService.modify(Mockito.anyString(), Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("test");
        mvc.perform(put("/group/{code}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.groupService.remove(Mockito.anyString());

        mvc.perform(delete("/group/{code}", "test").with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.groupService).remove(Mockito.anyString());

        mvc.perform(delete("/group/{code}", "test").with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void accounts() throws Exception {
        given(this.accountGroupService.accounts(Mockito.anyString())).willReturn(Mockito.anyList());

        mvc.perform(get("/group/{code}/account", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void accounts_error() throws Exception {
        doThrow(new RuntimeException()).when(this.accountGroupService).accounts(Mockito.anyString());

        mvc.perform(get("/group/{code}/account", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void tree() throws Exception {
        TreeNode treeNode = new TreeNode("test", "test");
        given(this.groupService.tree()).willReturn(Collections.singletonList(treeNode));

        mvc.perform(get("/group/tree")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void tree_error() throws Exception {
        given(this.groupService.tree()).willThrow(new RuntimeException());

        mvc.perform(get("/group/tree")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }
}