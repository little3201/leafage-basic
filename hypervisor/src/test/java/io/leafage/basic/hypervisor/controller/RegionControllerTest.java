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

package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * region controller test
 *
 * @author wq li 2021/12/7 15:38
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(RegionController.class)
class RegionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RegionService regionService;

    private RegionVO regionVO;

    private RegionDTO regionDTO;

    @BeforeEach
    void init() {
        regionVO = new RegionVO();
        regionVO.setName("test");
        regionVO.setAreaCode("23234");
        regionVO.setPostalCode(1212);
        regionVO.setDescription("description");

        regionDTO = new RegionDTO();
        regionDTO.setName("test");
        regionDTO.setAreaCode("23234");
        regionDTO.setPostalCode(1212);
        regionDTO.setSuperiorId(1L);
        regionDTO.setDescription("description");
    }

    @Test
    void retrieve() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        Page<RegionVO> voPage = new PageImpl<>(List.of(regionVO), pageable, 2L);
        given(this.regionService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(voPage);

        mvc.perform(get("/regions").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sortBy", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.regionService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        mvc.perform(get("/regions").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sortBy", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        given(this.regionService.fetch(Mockito.anyLong())).willReturn(regionVO);

        mvc.perform(get("/regions/{id}", Mockito.anyLong())).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.regionService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/regions/{id}", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void exist() throws Exception {
        given(this.regionService.exist(Mockito.anyString())).willReturn(true);

        mvc.perform(get("/regions/{name}/exist", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void exist_error() throws Exception {
        given(this.regionService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/regions/{name}/exist", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void subset() throws Exception {
        given(this.regionService.subset(Mockito.anyLong())).willReturn(List.of(regionVO));

        mvc.perform(get("/regions/{id}/subset", Mockito.anyLong())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void lower_error() throws Exception {
        given(this.regionService.subset(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/regions/{id}/subset", Mockito.anyLong())).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.regionService.create(Mockito.any(RegionDTO.class))).willReturn(regionVO);

        mvc.perform(post("/regions").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.regionService.create(Mockito.any(RegionDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/regions").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        given(this.regionService.modify(Mockito.anyLong(), Mockito.any(RegionDTO.class))).willReturn(regionVO);

        mvc.perform(put("/regions/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.regionService.modify(Mockito.anyLong(), Mockito.any(RegionDTO.class))).willThrow(new RuntimeException());

        mvc.perform(put("/regions/{id}", Mockito.anyLong()).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.regionService.remove(Mockito.anyLong());

        mvc.perform(delete("/regions/{id}", Mockito.anyLong()).with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.regionService).remove(Mockito.anyLong());

        mvc.perform(delete("/regions/{id}", Mockito.anyLong()).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}