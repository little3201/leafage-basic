package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.NotificationDTO;
import io.leafage.basic.hypervisor.service.NotificationService;
import io.leafage.basic.hypervisor.vo.NotificationVO;
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
 * notification controller test
 *
 * @author liwenqiang 2022/3/3 11:18
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private NotificationService notificationService;

    @Test
    void retrieve() throws Exception {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("标题");
        notificationVO.setReceiver("test");
        notificationVO.setContent("测试内容");
        Page<NotificationVO> voPage = new PageImpl<>(List.of(notificationVO));
        given(this.notificationService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(voPage);

        mvc.perform(get("/notification").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.notificationService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        mvc.perform(get("/notification").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setReceiver("test");
        notificationVO.setContent("测试内容");
        given(this.notificationService.fetch(Mockito.anyString())).willReturn(notificationVO);

        mvc.perform(get("/notification/{code}", "213ADJ09")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("测试内容")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.notificationService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/notification/{code}", "213ADJ09")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle("test");
        given(this.notificationService.create(Mockito.any(NotificationDTO.class))).willReturn(notificationVO);

        // 构造请求对象
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("test");
        notificationDTO.setReceiver("23234");
        notificationDTO.setContent("描述");
        mvc.perform(post("/notification").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(notificationDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.notificationService.create(Mockito.any(NotificationDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("test");
        notificationDTO.setReceiver("23234");
        notificationDTO.setContent("描述");
        mvc.perform(post("/notification").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(notificationDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}