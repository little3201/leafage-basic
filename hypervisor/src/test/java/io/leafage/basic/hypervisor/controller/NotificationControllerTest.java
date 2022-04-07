package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}