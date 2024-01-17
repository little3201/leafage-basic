/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostContentVO;
import io.leafage.basic.assets.vo.PostVO;
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

import java.util.Collections;
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
 * 帖子接口测试类
 *
 * @author liwenqiang 2019/9/14 21:46
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PostsService postsService;

    private PostDTO postDTO;
    private PostVO postVO;
    PostContentVO contentVO;

    @BeforeEach
    void init() {
        // 构造请求对象
        postDTO = new PostDTO();
        postDTO.setTitle("test");
        postDTO.setCategoryId(1L);
        postDTO.setCover("../test.jpg");
        postDTO.setTags(Collections.singleton("java"));
        postDTO.setContent("内容信息");

        postVO = new PostVO();
        postVO.setTitle(postDTO.getTitle());
        postVO.setTags(postDTO.getTags());
        postVO.setCover(postDTO.getCover());

        contentVO = new PostContentVO();
        contentVO.setTitle("test");
        contentVO.setCatalog("目录");
    }

    @Test
    void retrieve_page() throws Exception {
        Page<PostVO> page = new PageImpl<>(List.of(postVO));
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(page);

        mvc.perform(get("/posts").queryParam("page", "0")
                        .queryParam("size", "2").queryParam("sort", "id")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_page_error() throws Exception {
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new NoSuchElementException());

        mvc.perform(get("/posts").queryParam("page", "0")
                        .queryParam("size", "2").queryParam("sort", "id")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        given(this.postsService.fetch(Mockito.anyLong())).willReturn(postVO);

        mvc.perform(get("/posts/{id}", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.postsService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());
        mvc.perform(get("/posts/{id}", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void next() throws Exception {
        given(this.postsService.next(Mockito.anyLong())).willReturn(postVO);

        mvc.perform(get("/posts/{id}/next", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void next_error() throws Exception {
        given(this.postsService.next(Mockito.anyLong())).willThrow(new RuntimeException());
        mvc.perform(get("/posts/{id}/next", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void previous() throws Exception {
        given(this.postsService.previous(Mockito.anyLong())).willReturn(postVO);

        mvc.perform(get("/posts/{id}/previous", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void previous_error() throws Exception {
        given(this.postsService.previous(Mockito.anyLong())).willThrow(new RuntimeException());
        mvc.perform(get("/posts/{id}/previous", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void details() throws Exception {
        given(this.postsService.details(Mockito.anyLong())).willReturn(contentVO);

        mvc.perform(get("/posts/{id}/details", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void details_error() throws Exception {
        given(this.postsService.details(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/posts/{id}/details", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void exist() throws Exception {
        given(this.postsService.exist(Mockito.anyString())).willReturn(true);

        mvc.perform(get("/posts/exist").queryParam("title", "spring")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void exist_error() throws Exception {
        given(this.postsService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/posts/exist").queryParam("title", "spring")).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.postsService.create(Mockito.any(PostDTO.class))).willReturn(postVO);

        mvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.postsService.create(Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDTO)).with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(this.postsService.modify(Mockito.anyLong(), Mockito.any(PostDTO.class))).willReturn(postVO);

        mvc.perform(put("/posts/{id}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.postsService.modify(Mockito.anyLong(), Mockito.any(PostDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/posts/{id}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.postsService.remove(Mockito.anyLong());
        mvc.perform(delete("/posts/{id}", "21389KO6").with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.postsService).remove(Mockito.anyLong());
        mvc.perform(delete("/posts/{id}", "21389KO6").with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

}
