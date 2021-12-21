package io.leafage.basic.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.service.ResourceService;
import io.leafage.basic.assets.vo.ResourceVO;
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
import java.util.List;
import java.util.NoSuchElementException;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Resource 接口测试
 *
 * @author liwenqiang 2021/12/7 17:54
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ResourceService resourceService;

    @Test
    void retrieve_page() throws Exception {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("java");
        Page<ResourceVO> page = new PageImpl<>(List.of(resourceVO));
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.isNull())).willReturn(page);

        mvc.perform(get("/resource").queryParam("page", "0").queryParam("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_page_error() throws Exception {
        given(this.resourceService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
                .willThrow(new NoSuchElementException());

        mvc.perform(get("/resource").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("category", "21389KO6"))
                .andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("java");

        given(this.resourceService.fetch(Mockito.anyString())).willReturn(resourceVO);

        mvc.perform(get("/resource/{code}", "21389KO6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("java")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.resourceService.fetch(Mockito.anyString())).willThrow(new NoSuchElementException());

        mvc.perform(get("/resource/{code}", "21389KO6"))
                .andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void exist() throws Exception {
        given(this.resourceService.exist(Mockito.anyString())).willReturn(true);

        mvc.perform(get("/resource/exist").queryParam("title", "java")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }


    @Test
    void exist_error() throws Exception {
        given(this.resourceService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/resource/exist").queryParam("title", "java")).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("java");
        given(this.resourceService.create(Mockito.any(ResourceDTO.class))).willReturn(resourceVO);

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("java");
        resourceDTO.setCategory("21389KO6");
        resourceDTO.setCover("/avatar.jpg");
        resourceDTO.setType('E');
        resourceDTO.setDescription("描述");
        mvc.perform(post("/resource").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(resourceDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("java")).andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.resourceService.create(Mockito.any(ResourceDTO.class))).willThrow(new RuntimeException());

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("java");
        resourceDTO.setCategory("21389KO6");
        resourceDTO.setCover("/avatar.jpg");
        resourceDTO.setType('E');
        resourceDTO.setDescription("描述");
        mvc.perform(post("/resource").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(resourceDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed()).andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setTitle("java");
        given(this.resourceService.modify(Mockito.anyString(), Mockito.any(ResourceDTO.class))).willReturn(resourceVO);

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("java");
        resourceDTO.setCategory("21389KO6");
        resourceDTO.setCover("/avatar.jpg");
        resourceDTO.setType('E');
        resourceDTO.setDescription("描述");
        mvc.perform(put("/resource/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(resourceDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.title").value("java")).andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.resourceService.modify(Mockito.anyString(), Mockito.any(ResourceDTO.class))).willThrow(new RuntimeException());

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("java");
        resourceDTO.setCategory("21389KO6");
        resourceDTO.setCover("/avatar.jpg");
        resourceDTO.setType('E');
        resourceDTO.setDescription("描述");
        mvc.perform(put("/resource/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(resourceDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified()).andDo(print()).andReturn();
    }
}