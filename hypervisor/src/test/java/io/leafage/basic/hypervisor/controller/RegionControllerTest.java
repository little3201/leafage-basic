package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.GroupDTO;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.RegionVO;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * region controller test
 *
 * @author liwenqiang 2021/12/7 15:38
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

    @Test
    void exist() throws Exception {
        given(this.regionService.exist(Mockito.anyString())).willReturn(true);

        mvc.perform(get("/region/{name}/exist", "test")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void exist_error() throws Exception {
        given(this.regionService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/region/{name}/exist", "test")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void lower() throws Exception {
        RegionVO regionVO = new RegionVO();
        regionVO.setName("下一级");
        regionVO.setCode(1101L);
        given(this.regionService.lower(Mockito.anyLong())).willReturn(List.of(regionVO));

        mvc.perform(get("/region/{code}/lower", "11")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void lower_error() throws Exception {
        given(this.regionService.lower(Mockito.anyLong())).willThrow(new RuntimeException());

        mvc.perform(get("/region/{code}/lower", "11")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        // 构造返回对象
        RegionVO regionVO = new RegionVO();
        regionVO.setName("test");
        given(this.regionService.create(Mockito.any(RegionDTO.class))).willReturn(regionVO);

        // 构造请求对象
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("test");
        regionDTO.setAreaCode("23234");
        regionDTO.setDescription("描述");
        mvc.perform(post("/region").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.regionService.create(Mockito.any(RegionDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("test");
        mvc.perform(post("/region").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }

    @Test
    void modify() throws Exception {
        // 构造返回对象
        RegionVO regionVO = new RegionVO();
        regionVO.setName("test");
        given(this.regionService.modify(Mockito.anyLong(), Mockito.any(RegionDTO.class))).willReturn(regionVO);

        // 构造请求对象
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("test");
        mvc.perform(put("/region/{code}", "11").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isAccepted())
                .andDo(print()).andReturn();
    }

    @Test
    void modify_error() throws Exception {
        given(this.regionService.modify(Mockito.anyLong(), Mockito.any(RegionDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("test");
        mvc.perform(put("/region/{code}", "11").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regionDTO)).with(csrf().asHeader()))
                .andExpect(status().isNotModified())
                .andDo(print()).andReturn();
    }

    @Test
    void remove() throws Exception {
        this.regionService.remove(Mockito.anyLong());

        mvc.perform(delete("/region/{code}", "11").with(csrf().asHeader())).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void remove_error() throws Exception {
        doThrow(new RuntimeException()).when(this.regionService).remove(Mockito.anyLong());

        mvc.perform(delete("/region/{code}", "11").with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}