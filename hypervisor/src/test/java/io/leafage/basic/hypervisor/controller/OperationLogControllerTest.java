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
import io.leafage.basic.hypervisor.dto.OperationLogDTO;
import io.leafage.basic.hypervisor.service.OperationLogService;
import io.leafage.basic.hypervisor.vo.OperationLogVO;
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
 * operation log controller test
 *
 * @author wq li
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(OperationLogController.class)
class OperationLogControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OperationLogService operationLogService;

    private OperationLogVO operationLogVO;

    private OperationLogDTO operationLogDTO;

    @BeforeEach
    void init() {
        // 构造请求对象
        operationLogDTO = new OperationLogDTO();
        operationLogDTO.setIp("12.1.3.2");
        operationLogDTO.setLocation("test");
        operationLogDTO.setBrowser("Chrome");
        operationLogDTO.setDeviceType("PC");
        operationLogDTO.setContent("Content");
        operationLogDTO.setOs("Mac OS");
        operationLogDTO.setReferer("test");
        operationLogDTO.setBrowser("edge");
        operationLogDTO.setSessionId("sessionId");
        operationLogDTO.setStatusCode(200);
        operationLogDTO.setOperation("Change password");
        operationLogDTO.setUserAgent("xxx");

        // vo
        operationLogVO = new OperationLogVO();
        operationLogVO.setIp("12.1.3.2");
        operationLogVO.setLocation("test");
        operationLogVO.setBrowser("Chrome");
        operationLogVO.setDeviceType("PC");
        operationLogVO.setBrowser("Edge");
        operationLogVO.setOs("Mac OS");
        operationLogVO.setReferer("test");
        operationLogVO.setContent("content");
        operationLogVO.setSessionId("sessionId");
        operationLogVO.setStatusCode(200);
        operationLogVO.setOperation("test");
        operationLogVO.setUserAgent("xxx");
    }

    @Test
    void retrieve() throws Exception {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Page<OperationLogVO> voPage = new PageImpl<>(List.of(operationLogVO), pageable, 2L);
        given(this.operationLogService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean())).willReturn(voPage);

        mvc.perform(get("/operation-logs").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sortBy", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.operationLogService.retrieve(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean())).willThrow(new RuntimeException());

        mvc.perform(get("/operation-logs").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sortBy", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.operationLogService.create(Mockito.any(OperationLogDTO.class))).willReturn(operationLogVO);

        mvc.perform(post("/operation-logs").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(operationLogDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.location").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.operationLogService.create(Mockito.any(OperationLogDTO.class))).willThrow(new RuntimeException());

        mvc.perform(post("/operation-logs").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(operationLogDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}