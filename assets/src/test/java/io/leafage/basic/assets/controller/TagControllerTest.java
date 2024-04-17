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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.TagDTO;
import io.leafage.basic.assets.service.TagService;
import io.leafage.basic.assets.vo.TagVO;
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
 * tag 接口测试
 *
 * @author wq li 2019/9/14 21:46
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TagService tagService;

    private TagVO tagVO;

    private TagDTO tagDTO;

    @BeforeEach
    void init() {
        tagVO = new TagVO();
        tagVO.setName("test");
        tagVO.setCount(21L);

        tagDTO = new TagDTO();
        tagDTO.setName("test");
    }

    @Test
    void retrieve() throws Exception {
        Pageable pageable = PageRequest.of(0,2);
        Page<TagVO> page = new PageImpl<>(List.of(tagVO), pageable, 2L);
        given(tagService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willReturn(page);

        mvc.perform(get("/tags").queryParam("page", "0")
                        .queryParam("size", "2").queryParam("sort", "id"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(tagService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).willThrow(new NoSuchElementException());

        mvc.perform(get("/tags").queryParam("page", "0")
                        .queryParam("size", "2").queryParam("sort", "id")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        given(tagService.fetch(Mockito.anyLong())).willReturn(tagVO);

        mvc.perform(get("/tags/{id}", 1L)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(tagService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/tags/{id}", 1L)).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(tagService.create(Mockito.any(TagDTO.class))).willReturn(tagVO);

        mvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tagDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(tagService.create(Mockito.any(TagDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/tags").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tagDTO)).with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(tagService.modify(Mockito.anyLong(), Mockito.any(TagDTO.class))).willReturn(tagVO);

        mvc.perform(put("/tags/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tagDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(tagService.modify(Mockito.anyLong(), Mockito.any(TagDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/tags/{id}", Mockito.anyLong()).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tagDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        tagService.remove(Mockito.anyLong());

        mvc.perform(delete("/tags/{id}", Mockito.anyLong()).with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(tagService).remove(Mockito.anyLong());

        mvc.perform(delete("/tags/{id}", Mockito.anyLong()).with(csrf().asHeader())).andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}