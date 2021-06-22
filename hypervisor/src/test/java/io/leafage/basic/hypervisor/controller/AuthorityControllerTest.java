package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
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
 * 权限接口测试类
 *
 * @author liwenqiang 2019/9/14 21:52
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorityController.class)
class AuthorityControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorityService authorityService;

    @Test
    void retrieve() throws Exception {
        List<AuthorityVO> voList = new ArrayList<>(2);
        voList.add(new AuthorityVO());
        Page<AuthorityVO> voPage = new PageImpl<>(voList);
        given(this.authorityService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(voPage);

        mvc.perform(get("/authority").queryParam("page", "0").queryParam("size", "2")
                .queryParam("order", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.authorityService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/authority").queryParam("page", "0").queryParam("size", "2")
                .queryParam("order", "")).andExpect(status().is(204)).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.fetch(Mockito.anyString())).willReturn(authorityVO);

        mvc.perform(get("/authority/{code}", "test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.authorityService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/authority/{code}", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.create(Mockito.any(AuthorityDTO.class))).willReturn(authorityVO);

        // 构造请求对象
        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setPath("/test");
        mvc.perform(post("/authority").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authorityDTO))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.authorityService.create(Mockito.any(AuthorityDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setPath("/test");
        mvc.perform(post("/authority").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authorityDTO))).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        AuthorityVO authorityVO = new AuthorityVO();
        authorityVO.setName("test");
        given(this.authorityService.modify(Mockito.anyString(), Mockito.any(AuthorityDTO.class))).willReturn(authorityVO);

        // 构造请求对象
        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setPath("/test");
        mvc.perform(put("/authority/{code}", "test").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authorityDTO)))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.authorityService.modify(Mockito.anyString(), Mockito.any(AuthorityDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setName("test");
        authorityDTO.setType('M');
        authorityDTO.setPath("/test");
        mvc.perform(put("/authority/{code}", "test").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authorityDTO)))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.authorityService.remove(Mockito.anyString());

        mvc.perform(delete("/authority/{code}", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.authorityService).remove(Mockito.anyString());

        mvc.perform(delete("/authority/{code}", "test")).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}