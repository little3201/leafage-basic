/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
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
import java.util.Set;
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
@WebMvcTest(PostsController.class)
class PostsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PostsService postsService;

    @Test
    void retrieve_page() throws Exception {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        postsVO.setCategory("Technology");
        Page<PostsVO> page = new PageImpl<>(List.of(postsVO));
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
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.fetch(Mockito.anyString())).willReturn(postsVO);

        mvc.perform(get("/posts/{code}", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.postsService.fetch(Mockito.anyString())).willThrow(new RuntimeException());
        mvc.perform(get("/posts/{code}", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void next() throws Exception {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.next(Mockito.anyString())).willReturn(postsVO);

        mvc.perform(get("/posts/{code}/next", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void next_error() throws Exception {
        given(this.postsService.next(Mockito.anyString())).willThrow(new RuntimeException());
        mvc.perform(get("/posts/{code}/next", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void previous() throws Exception {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.previous(Mockito.anyString())).willReturn(postsVO);

        mvc.perform(get("/posts/{code}/previous", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void previous_error() throws Exception {
        given(this.postsService.previous(Mockito.anyString())).willThrow(new RuntimeException());
        mvc.perform(get("/posts/{code}/previous", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void details() throws Exception {
        PostsContentVO contentVO = new PostsContentVO();
        contentVO.setTitle("test");
        contentVO.setCatalog("目录");
        given(this.postsService.details(Mockito.anyString())).willReturn(contentVO);

        mvc.perform(get("/posts/{code}/details", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void details_error() throws Exception {
        given(this.postsService.details(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/posts/{code}/details", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void content() throws Exception {
        ContentVO contentVO = new ContentVO();
        contentVO.setContent("test");
        contentVO.setCatalog("目录");
        given(this.postsService.content(Mockito.anyString())).willReturn(contentVO);

        mvc.perform(get("/posts/{code}/content", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("test")).andDo(print()).andReturn();
    }

    @Test
    void content_error() throws Exception {
        given(this.postsService.content(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/posts/{code}/content", "21389KO6")).andExpect(status().isNoContent())
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
        // 构造返回对象
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        postsVO.setTags(Set.of("tag"));
        given(this.postsService.create(Mockito.any(PostsDTO.class))).willReturn(postsVO);

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("2138JJO6");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setContent("java");
        mvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postsDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.postsService.create(Mockito.any(PostsDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("2138JJO6");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setContent("java");
        mvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postsDTO)).with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostsDTO.class))).willReturn(postsVO);

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("2138JJO6");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setContent("java");
        mvc.perform(put("/posts/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postsDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostsDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("2138JJO6");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setContent("java");
        mvc.perform(put("/posts/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postsDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.postsService.remove(Mockito.anyString());
        mvc.perform(delete("/posts/{code}", "21389KO6").with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.postsService).remove(Mockito.anyString());
        mvc.perform(delete("/posts/{code}", "21389KO6").with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

}
