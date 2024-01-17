package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.PrivilegeDTO;
import io.leafage.basic.hypervisor.service.PrivilegeService;
import io.leafage.basic.hypervisor.service.RolePrivilegesService;
import io.leafage.basic.hypervisor.vo.PrivilegeVO;
import org.junit.jupiter.api.BeforeEach;
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
 * privilege controller test
 *
 * @author liwenqiang 2019/9/14 21:52
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(PrivilegeController.class)
class PrivilegeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PrivilegeService privilegeService;

    @MockBean
    private RolePrivilegesService rolePrivilegesService;

    private PrivilegeVO privilegeVO;

    private PrivilegeDTO privilegeDTO;

    @BeforeEach
    void init() {
        privilegeVO = new PrivilegeVO();
        privilegeVO.setSuperior("superior");

        privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setName("test");
        privilegeDTO.setType('M');
        privilegeDTO.setPath("/test");
    }

    @Test
    void retrieve() throws Exception {
        Page<PrivilegeVO> voPage = new PageImpl<>(List.of(privilegeVO));
        given(this.privilegeService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

        mvc.perform(get("/privileges").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.privilegeService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/privileges").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        given(this.privilegeService.fetch(Mockito.anyLong())).willReturn(privilegeVO);

        mvc.perform(get("/privileges/{id}", "213ADJ09")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.privilegeService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/privileges/{id}", "213ADJ09")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.privilegeService.create(Mockito.any(PrivilegeDTO.class))).willReturn(privilegeVO);

        mvc.perform(post("/privileges").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(privilegeDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.privilegeService.create(Mockito.any(PrivilegeDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/privileges").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(privilegeDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(this.privilegeService.modify(Mockito.anyLong(), Mockito.any(PrivilegeDTO.class))).willReturn(privilegeVO);

        mvc.perform(put("/privileges/{id}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(privilegeDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.privilegeService.modify(Mockito.anyLong(), Mockito.any(PrivilegeDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/privileges/{id}", "test").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(privilegeDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.privilegeService.remove(Mockito.anyLong());

        mvc.perform(delete("/privileges/{id}", "test").with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.privilegeService).remove(Mockito.anyLong());

        mvc.perform(delete("/privileges/{id}", "test").with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void tree() throws Exception {
        TreeNode treeNode = new TreeNode(1L, "test");
        given(this.privilegeService.tree()).willReturn(Collections.singletonList(treeNode));

        mvc.perform(get("/privileges/tree")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void tree_error() throws Exception {
        given(this.privilegeService.tree()).willThrow(new RuntimeException());

        mvc.perform(get("/privileges/tree")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void roles() throws Exception {
        given(this.rolePrivilegesService.roles(Mockito.anyLong())).willReturn(Mockito.anyList());

        mvc.perform(get("/privileges/{id}/roles", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void roles_error() throws Exception {
        given(this.rolePrivilegesService.roles(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/privileges/{id}/roles", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }
}