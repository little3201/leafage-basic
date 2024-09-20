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
import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * access log controller test
 *
 * @author wq li
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(AccessLogController.class)
class AccessLogControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccessLogService accessLogService;

    private AccessLogVO accessLogVO;

    private AccessLogDTO accessLogDTO;

    @BeforeEach
    void init() {
        // 构造请求对象
        accessLogDTO = new AccessLogDTO();
        accessLogDTO.setIp("12.1.3.2");
        accessLogDTO.setLocation("test");
        accessLogDTO.setBrowser("Chrome");
        accessLogDTO.setDeviceType("PC");
        accessLogDTO.setHttpMethod("POST");
        accessLogDTO.setOs("Mac OS");
        accessLogDTO.setReferer("test");
        accessLogDTO.setResponseTime(232L);
        accessLogDTO.setSessionId("sessionId");
        accessLogDTO.setStatusCode(200);
        accessLogDTO.setUrl("/test");
        accessLogDTO.setUserAgent("xxx");

        // vo
        accessLogVO = new AccessLogVO();
        accessLogVO.setIp("12.1.3.2");
        accessLogVO.setLocation("test");
        accessLogVO.setBrowser("Chrome");
        accessLogVO.setDeviceType("PC");
        accessLogVO.setHttpMethod("POST");
        accessLogVO.setOs("Mac OS");
        accessLogVO.setReferer("test");
        accessLogVO.setResponseTime(232L);
        accessLogVO.setSessionId("sessionId");
        accessLogVO.setStatusCode(200);
        accessLogVO.setUrl("/test");
        accessLogVO.setUserAgent("xxx");
    }

    @Test
    void retrieve() throws Exception {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Page<AccessLogVO> voPage = new PageImpl<>(List.of(accessLogVO), pageable, 2L);
        given(this.accessLogService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean())).willReturn(voPage);

        mvc.perform(get("/access-logs").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sortBy", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.accessLogService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean())).willThrow(new RuntimeException());

        mvc.perform(get("/access-logs").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sortBy", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.accessLogService.create(Mockito.any(AccessLogDTO.class))).willReturn(accessLogVO);

        mvc.perform(post("/access-logs").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accessLogDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.location").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.accessLogService.create(Mockito.any(AccessLogDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/access-logs").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accessLogDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}