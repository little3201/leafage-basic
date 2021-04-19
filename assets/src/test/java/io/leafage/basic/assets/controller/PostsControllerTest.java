/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostsVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

}
