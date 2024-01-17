package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.PostStatisticsService;
import io.leafage.basic.assets.vo.PostStatisticsVO;
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
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Statistics 接口测试
 *
 * @author liwenqiang 2021/12/7 17:54
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(PostStatisticsController.class)
class PostPostStatisticsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostStatisticsService postStatisticsService;

    @Test
    void retrieve() throws Exception {
        PostStatisticsVO postStatisticsVO = new PostStatisticsVO();
        postStatisticsVO.setDate(LocalDate.now());
        Page<PostStatisticsVO> page = new PageImpl<>(List.of(postStatisticsVO));
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