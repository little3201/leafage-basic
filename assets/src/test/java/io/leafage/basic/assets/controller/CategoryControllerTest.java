package io.leafage.basic.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
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

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * category 接口测试
 *
 * @author liwenqiang 2019/9/14 21:46
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoryService categoryService;

    private CategoryVO categoryVO;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void init() {
        categoryVO = new CategoryVO();
        categoryVO.setName("test");
        categoryVO.setCount(21L);

        categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
    }

    @Test
    void retrieve() throws Exception {
        Page<CategoryVO> page = new PageImpl<>(List.of(categoryVO));
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(page);

        mvc.perform(get("/category").queryParam("page", "0")
                        .queryParam("size", "2").queryParam("sort", "id"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new NoSuchElementException());

        mvc.perform(get("/category").queryParam("page", "0")
                        .queryParam("size", "2").queryParam("sort", "id")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        given(this.categoryService.fetch(Mockito.anyLong())).willReturn(categoryVO);

        mvc.perform(get("/category/{id}", 1L)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.categoryService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/category/{id}", 1L)).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willReturn(categoryVO);

        mvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDTO)).with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(this.categoryService.modify(Mockito.anyLong(), Mockito.any(CategoryDTO.class))).willReturn(categoryVO);

        mvc.perform(put("/category/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.categoryService.modify(Mockito.anyLong(), Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/category/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.categoryService.remove(Mockito.anyLong());

        mvc.perform(delete("/category/{id}", 1L).with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.categoryService).remove(Mockito.anyLong());

        mvc.perform(delete("/category/{id}", 1L).with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}