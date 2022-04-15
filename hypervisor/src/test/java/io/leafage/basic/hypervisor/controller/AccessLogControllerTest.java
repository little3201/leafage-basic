package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * @author liwenqiang 2022/3/3 11:18
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

    @Test
    void retrieve() throws Exception {
        AccessLogVO accessLogVO = new AccessLogVO();
        accessLogVO.setIp("12.1.3.2");
        accessLogVO.setLocation("test");
        accessLogVO.setDescription("描述");
        Page<AccessLogVO> voPage = new PageImpl<>(List.of(accessLogVO));
        given(this.accessLogService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(voPage);

        mvc.perform(get("/access-log").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.accessLogService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        mvc.perform(get("/access-log").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        AccessLogVO accessLogVO = new AccessLogVO();
        accessLogVO.setIp("12.1.3.2");
        accessLogVO.setLocation("test");
        accessLogVO.setDescription("描述");
        given(this.accessLogService.create(Mockito.any(AccessLogDTO.class))).willReturn(accessLogVO);

        // 构造请求对象
        AccessLogDTO accessLogDTO = new AccessLogDTO();
        accessLogDTO.setIp("12.1.3.2");
        accessLogDTO.setLocation("test");
        accessLogDTO.setDescription("描述");
        mvc.perform(post("/access-log").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accessLogDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.location").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.accessLogService.create(Mockito.any(AccessLogDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        AccessLogDTO accessLogDTO = new AccessLogDTO();
        accessLogDTO.setIp("12.1.3.2");
        accessLogDTO.setLocation("test");
        accessLogDTO.setDescription("描述");
        mvc.perform(post("/access-log").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accessLogDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}