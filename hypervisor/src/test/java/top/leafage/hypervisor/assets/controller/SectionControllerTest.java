/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.assets.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.service.SectionService;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Section controller test
 *
 * @author wq li
 **/
@WithMockUser
@WebMvcTest(SectionController.class)
class SectionControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private SectionService sectionService;

    private SectionDTO dto;
    private SectionVO vo;

    @BeforeEach
    void setUp() {
        dto = new SectionDTO();
        dto.setName("test");
        dto.setSuperiorId(1L);
        dto.setBody(Collections.emptyMap());
        dto.setOwnerId(1L);
        dto.setOwnerType("TEMPLATE");

        vo = new SectionVO(1L, 1L, 1L, "test", 1, 2, null, 2L);
    }

    @Test
    void fetch() {
        when(sectionService.fetch(anyLong())).thenReturn(vo);

        assertThat(mvc.get().uri("/sections/{id}", anyLong()))
                .hasStatusOk()
                .bodyJson()
                .convertTo(SectionVO.class)
                .satisfies(vo -> assertThat(vo.name()).isEqualTo("test"));
    }

    @Test
    void fetch_error() {
        when(sectionService.fetch(anyLong())).thenThrow(new RuntimeException());

        assertThat(mvc.get().uri("/sections/{id}", anyLong()))
                .hasStatus5xxServerError();
    }

    @Test
    void create() {
        when(sectionService.create(any(SectionDTO.class))).thenReturn(vo);

        assertThat(mvc.post().uri("/sections").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)).with(csrf().asHeader()))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(SectionVO.class)
                .satisfies(vo -> assertThat(vo.name()).isEqualTo("test"));
    }

    @Test
    void create_error() {
        when(sectionService.create(any(SectionDTO.class))).thenThrow(new RuntimeException());

        assertThat(mvc.post().uri("/sections").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)).with(csrf().asHeader()))
                .hasStatus5xxServerError();
    }

    @Test
    void modify() {
        when(sectionService.modify(anyLong(), any(SectionDTO.class))).thenReturn(vo);

        assertThat(mvc.put().uri("/sections/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)).with(csrf().asHeader()))
                .hasStatus(HttpStatus.ACCEPTED)
                .bodyJson()
                .convertTo(SectionVO.class)
                .satisfies(vo -> assertThat(vo.name()).isEqualTo("test"));
    }

    @Test
    void modify_error() {
        when(sectionService.modify(anyLong(), any(SectionDTO.class))).thenThrow(new RuntimeException());

        assertThat(mvc.put().uri("/sections/{id}", anyLong()).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)).with(csrf().asHeader()))
                .hasStatus5xxServerError();
    }

    @Test
    void remove() {
        this.sectionService.remove(anyLong());

        assertThat(mvc.delete().uri("/sections/{id}", anyLong()).with(csrf().asHeader()))
                .hasStatusOk();
    }

    @Test
    void remove_error() {
        doThrow(new RuntimeException()).when(sectionService).remove(anyLong());

        assertThat(mvc.delete().uri("/sections/{id}", anyLong()).with(csrf().asHeader()))
                .hasStatus5xxServerError();
    }

}