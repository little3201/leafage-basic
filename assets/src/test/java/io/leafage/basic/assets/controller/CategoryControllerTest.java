package io.leafage.basic.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void retrieve() throws Exception {
        List<CategoryVO> voList = new ArrayList<>(2);
        voList.add(new CategoryVO());
        Page<CategoryVO> postsPage = new PageImpl<>(voList);
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(postsPage);
        mvc.perform(get("/category").queryParam("page", "0")
                .queryParam("size", "2").queryParam("order", "id"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
    }


    @Test
    public void retrieve_empty() throws Exception {
        Page<CategoryVO> postsPage = new PageImpl<>(Collections.emptyList());
        given(this.categoryService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(postsPage);
        mvc.perform(get("/category").queryParam("page", "0")
                .queryParam("size", "2").queryParam("order", "id")).andExpect(status().is(204))
                .andDo(print()).andReturn();
    }


    @Test
    void fetch() throws Exception {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setName("test");
        given(this.categoryService.fetch(Mockito.anyString())).willReturn(categoryVO);
        mvc.perform(get("/category/{code}", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_null() throws Exception {
        given(this.categoryService.fetch(Mockito.anyString())).willReturn(null);
        mvc.perform(get("/category/{code}", "21389KO6")).andExpect(status().is(204))
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setName("test");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willReturn(categoryVO);
        mvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setName("test");
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        given(this.categoryService.create(Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());
        mvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDTO))).andExpect(status().is(417))
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        given(this.categoryService.modify(Mockito.anyString(), Mockito.any(CategoryDTO.class))).willReturn(Mockito.mock(CategoryVO.class));
        mvc.perform(put("/category/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDTO)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        // 构造请求对象
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        given(this.categoryService.modify(Mockito.anyString(), Mockito.any(CategoryDTO.class))).willThrow(new RuntimeException());
        mvc.perform(put("/category/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDTO)))
                .andExpect(status().is(304))
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.categoryService.remove(Mockito.anyString());
        mvc.perform(delete("/category/{code}", "21389KO6")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.categoryService).remove(Mockito.anyString());
        mvc.perform(delete("/category/{code}", "21389KO6")).andExpect(status().is(417))
                .andDo(print()).andReturn();
    }
}