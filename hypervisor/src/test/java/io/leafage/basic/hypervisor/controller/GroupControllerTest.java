package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.service.GroupMembersService;
import io.leafage.basic.hypervisor.service.GroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
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
    private GroupMembersService groupMembersService;

    private GroupVO groupVO;

    private GroupDTO groupDTO;

    @BeforeEach
    void init() {
        groupVO = new GroupVO();
        groupVO.setName("test");
        groupVO.setAlias("alias");
        groupVO.setPrincipal("test");
        groupVO.setSuperior("superior");
        groupVO.setCount(2L);

        groupDTO = new GroupDTO();
        groupDTO.setName("test");
        groupDTO.setSuperiorId(1L);
        groupDTO.setPrincipal("test");
        groupDTO.setDescription("描述");
    }

    @Test
    void retrieve() throws Exception {
        Pageable pageable = PageRequest.of(0,2);
        Page<GroupVO> voPage = new PageImpl<>(List.of(groupVO), pageable, 2L);
        given(this.groupService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

        mvc.perform(get("/groups").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "id")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.groupService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/groups").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        given(this.groupService.fetch(Mockito.anyLong())).willReturn(groupVO);

        mvc.perform(get("/groups/{id}", Mockito.anyLong())).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.groupService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/groups/{id}", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willReturn(groupVO);

        mvc.perform(post("/groups").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.groupService.create(Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/groups").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(this.groupService.modify(Mockito.anyLong(), Mockito.any(GroupDTO.class))).willReturn(groupVO);

        mvc.perform(put("/groups/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.groupService.modify(Mockito.anyLong(), Mockito.any(GroupDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/groups/{id}", Mockito.anyLong()).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(groupDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.groupService.remove(Mockito.anyLong());

        mvc.perform(delete("/groups/{id}", Mockito.anyLong()).with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.groupService).remove(Mockito.anyLong());

        mvc.perform(delete("/groups/{id}", Mockito.anyLong()).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void members() throws Exception {
        given(this.groupMembersService.members(Mockito.anyLong())).willReturn(Mockito.anyList());

        mvc.perform(get("/groups/{id}/members", 1L)).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void members_error() throws Exception {
        doThrow(new RuntimeException()).when(this.groupMembersService).members(Mockito.anyLong());

        mvc.perform(get("/groups/{id}/members", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void tree() throws Exception {
        TreeNode treeNode = new TreeNode(1L, "test");
        given(this.groupService.tree()).willReturn(Collections.singletonList(treeNode));

        mvc.perform(get("/groups/tree")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void tree_error() throws Exception {
        given(this.groupService.tree()).willThrow(new RuntimeException());

        mvc.perform(get("/groups/tree")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }
}