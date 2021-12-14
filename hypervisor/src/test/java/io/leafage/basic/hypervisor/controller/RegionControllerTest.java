package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
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
 * Region 接口测试
 *
 * @author liwenqiang 2021/12/7 15:38
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(RegionController.class)
class RegionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegionService regionService;

    @Test
    void retrieve() throws Exception {
        RegionVO regionVO = new RegionVO();
        regionVO.setSuperior("北京市");
        Page<RegionVO> voPage = new PageImpl<>(List.of(regionVO));
        given(this.regionService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(voPage);

        mvc.perform(get("/region").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.regionService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        mvc.perform(get("/region").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        RegionVO regionVO = new RegionVO();
        regionVO.setName("北京市");
        given(this.regionService.fetch(Mockito.anyLong())).willReturn(regionVO);

        mvc.perform(get("/region/{code}", "11")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("北京市")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.regionService.fetch(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/region/{code}", "11")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }
}