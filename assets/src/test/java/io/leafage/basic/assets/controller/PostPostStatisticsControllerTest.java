/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.PostStatisticsService;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * post statistics 接口测试
 *
 * @author wq li 2021/12/7 17:54
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(PostStatisticsController.class)
class PostPostStatisticsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostStatisticsService postStatisticsService;

    private PostStatisticsVO postStatisticsVO;

    @BeforeEach
    void init() {
        postStatisticsVO = new PostStatisticsVO();
        postStatisticsVO.setPostId(1L);
        postStatisticsVO.setComments(12);
        postStatisticsVO.setOverComments(23.23);
        postStatisticsVO.setLikes(23);
        postStatisticsVO.setOverLikes(23.2);
        postStatisticsVO.setViewed(23);
        postStatisticsVO.setOverViewed(0.23);
    }

    @Test
    void retrieve() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        Page<PostStatisticsVO> page = new PageImpl<>(List.of(postStatisticsVO), pageable, 2L);
        given(this.postStatisticsService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(page);

        mvc.perform(get("/statistics").queryParam("page", "0").queryParam("size", "2"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.postStatisticsService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new NoSuchElementException());

        mvc.perform(get("/statistics").queryParam("page", "0").queryParam("size", "2"))
                .andExpect(status().isNoContent()).andDo(print()).andReturn();
    }
}