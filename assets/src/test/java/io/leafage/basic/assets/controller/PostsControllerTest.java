/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.service.PostsService;
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

/**
 * 帖子接口测试类
 *
 * @author liwenqiang 2019/9/14 21:46
 **/
@ExtendWith(SpringExtension.class)
@WebMvcTest(PostsController.class)
class PostsControllerTest {

    @MockBean
    private PostsService postsService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void retrieve() throws Exception {
        List<PostsVO> voList = new ArrayList<>(2);
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        voList.add(postsVO);
        voList.add(postsVO);
        Page<PostsVO> postsPage = new PageImpl<>(voList);
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(postsPage);

        mvc.perform(get("/posts").queryParam("page", "0")
                .queryParam("size", "2").queryParam("order", "id")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/posts").queryParam("page", "0")
                .queryParam("size", "2").queryParam("order", "id")).andExpect(status().isNoContent())
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
    void fetchDetails() throws Exception {
        PostsContentVO contentVO = new PostsContentVO();
        contentVO.setTitle("test");
        given(this.postsService.fetchDetails(Mockito.anyString())).willReturn(contentVO);

        mvc.perform(get("/posts/{code}/details", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetchDetails_error() throws Exception {
        given(this.postsService.fetchDetails(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/posts/{code}/details", "21389KO6")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.create(Mockito.any(PostsDTO.class))).willReturn(postsVO);

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("2138JJO6");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setContent("java");
        mvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(postsDTO))).andExpect(status().isCreated())
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
                .content(mapper.writeValueAsString(postsDTO))).andExpect(status().isExpectationFailed())
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
        postsDTO.setSubtitle("test line");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("2138JJO6");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setContent("java");
        mvc.perform(put("/posts/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(postsDTO)))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostsDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setSubtitle("test line");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategory("2138JJO6");
        postsDTO.setTags(Collections.singleton("java"));
        postsDTO.setContent("java");
        mvc.perform(put("/posts/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(postsDTO)))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.postsService.remove(Mockito.anyString());
        mvc.perform(delete("/posts/{code}", "21389KO6")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.postsService).remove(Mockito.anyString());
        mvc.perform(delete("/posts/{code}", "21389KO6")).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

}
