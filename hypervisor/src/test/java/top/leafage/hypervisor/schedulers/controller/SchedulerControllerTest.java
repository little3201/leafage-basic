/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.schedulers.controller;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;
import top.leafage.hypervisor.schedulers.domain.Scheduler;
import top.leafage.hypervisor.schedulers.domain.dto.SchedulerDTO;
import top.leafage.hypervisor.schedulers.domain.vo.SchedulerVO;
import top.leafage.hypervisor.schedulers.service.SchedulerService;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WithMockUser
@WebMvcTest(SchedulerController.class)
class SchedulerControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private SchedulerService schedulerService;

    private SchedulerDTO dto;
    private SchedulerVO vo;

    @BeforeEach
    void setUp() {
        dto = new SchedulerDTO();
        dto.setName("clear logs");
        dto.setCronExpression("0 0 0 * * ?");

        vo = new SchedulerVO(1L, "clear logs", "0 0 0 * * ?",
                Instant.parse("2026-08-07T00:00:00Z"), Scheduler.Status.SUCCEED,
                Instant.parse("2026-08-08T00:00:00Z"), "done");
    }

    @Test
    void retrieve() {
        Page<SchedulerVO> page = new PageImpl<>(List.of(vo), mock(PageRequest.class), 1L);
        when(schedulerService.retrieve(anyInt(), anyInt(), anyString(), anyBoolean(), anyString())).thenReturn(page);

        assertThat(mvc.get().uri("/schedulers")
                .queryParam("page", "0")
                .queryParam("size", "2")
                .queryParam("sortBy", "id")
                .queryParam("descending", "false")
                .queryParam("filters", "name:like:clear"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.content")
                .convertTo(InstanceOfAssertFactories.list(SchedulerVO.class))
                .hasSize(1)
                .element(0).satisfies(vo -> assertThat(vo.name()).isEqualTo("clear logs"));
    }

    @Test
    void fetch() {
        when(schedulerService.fetch(anyLong())).thenReturn(vo);

        assertThat(mvc.get().uri("/schedulers/{id}", 1L))
                .hasStatusOk()
                .bodyJson()
                .convertTo(SchedulerVO.class)
                .satisfies(vo -> assertThat(vo.cronExpression()).isEqualTo("0 0 0 * * ?"));
    }

    @Test
    void create() throws Exception {
        when(schedulerService.create(any(SchedulerDTO.class))).thenReturn(vo);

        assertThat(mvc.post().uri("/schedulers").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)).with(csrf().asHeader()))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(SchedulerVO.class)
                .satisfies(vo -> assertThat(vo.name()).isEqualTo("clear logs"));
    }

    @Test
    void modify() throws Exception {
        when(schedulerService.modify(anyLong(), any(SchedulerDTO.class))).thenReturn(vo);

        assertThat(mvc.put().uri("/schedulers/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)).with(csrf().asHeader()))
                .hasStatus(HttpStatus.ACCEPTED)
                .bodyJson()
                .convertTo(SchedulerVO.class)
                .satisfies(vo -> assertThat(vo.status()).isEqualTo(Scheduler.Status.SUCCEED));
    }

    @Test
    void remove() {
        assertThat(mvc.delete().uri("/schedulers/{id}", 1L).with(csrf().asHeader()))
                .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void enable() {
        when(schedulerService.enable(anyLong())).thenReturn(true);

        assertThat(mvc.patch().uri("/schedulers/{id}/enable", 1L).with(csrf().asHeader()))
                .hasStatusOk()
                .bodyJson()
                .convertTo(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    void disable() {
        when(schedulerService.disable(anyLong())).thenReturn(true);

        assertThat(mvc.patch().uri("/schedulers/{id}/disable", 1L).with(csrf().asHeader()))
                .hasStatusOk()
                .bodyJson()
                .convertTo(Boolean.class)
                .isEqualTo(true);
    }
}
