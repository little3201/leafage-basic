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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
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
public class PostsControllerTest {

    @MockBean
    private PostsService postsService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void retrieve() throws Exception {
        List<PostsVO> voList = new ArrayList<>(2);
        voList.add(new PostsVO());
        Page<PostsVO> postsPage = new PageImpl<>(voList);
        given(this.postsService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(postsPage);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("page", "0");
        multiValueMap.add("size", "2");
        multiValueMap.add("order", "id");
        mvc.perform(get("/posts").queryParams(multiValueMap)).andExpect(status().isOk()).andDo(print()).andReturn();
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
    void fetchDetails() throws Exception {
        PostsContentVO contentVO = new PostsContentVO();
        contentVO.setTitle("test");
        given(this.postsService.fetchDetails(Mockito.anyString())).willReturn(contentVO);
        mvc.perform(get("/posts/{code}/details", "21389KO6")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test")).andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategoryId(1L);
        // 构造返回对象
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle("test");
        given(this.postsService.create(Mockito.any(PostsDTO.class))).willReturn(postsVO);
        mvc.perform(post("/posts").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(postsDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造请求对象
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setTitle("test");
        postsDTO.setSubtitle("test line");
        postsDTO.setCover("../test.jpg");
        postsDTO.setCategoryId(1L);
        given(this.postsService.modify(Mockito.anyString(), Mockito.any(PostsDTO.class))).willReturn(Mockito.mock(PostsVO.class));
        mvc.perform(put("/posts/{code}", "21389KO6").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(postsDTO)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.postsService.remove(Mockito.anyString());
        mvc.perform(delete("/posts/{code}", "21389KO6")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

}
