package io.leafage.basic.hypervisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * dictionary controller test
 *
 * @author liwenqiang 2022-04-07 9:19
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(DictionaryController.class)
class DictionaryControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DictionaryService dictionaryService;

    @Test
    void retrieve() throws Exception {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("性别");
        Page<DictionaryVO> voPage = new PageImpl<>(List.of(dictionaryVO));
        given(this.dictionaryService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(voPage);

        mvc.perform(get("/dictionary").queryParam("page", "0").queryParam("size", "2")
                        .queryParam("sort", "")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.dictionaryService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new RuntimeException());

        mvc.perform(get("/dictionary").queryParam("page", "0").queryParam("size", "2")
                .queryParam("sort", "")).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void fetch() throws Exception {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("gender");
        given(this.dictionaryService.fetch(Mockito.anyString())).willReturn(dictionaryVO);

        mvc.perform(get("/dictionary/{code}", "2247JD0K")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("gender")).andDo(print()).andReturn();
    }

    @Test
    void fetch_error() throws Exception {
        given(this.dictionaryService.fetch(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/dictionary/{code}", "2247JD0K")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void exist() throws Exception {
        given(this.dictionaryService.exist(Mockito.anyString())).willReturn(true);

        mvc.perform(get("/dictionary/{name}/exist", "gender")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void exist_error() throws Exception {
        given(this.dictionaryService.exist(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/dictionary/{name}/exist", "gender")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }


    @Test
    void lower() throws Exception {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("性别");
        given(this.dictionaryService.lower(Mockito.anyString())).willReturn(List.of(dictionaryVO));

        mvc.perform(get("/dictionary/{code}/lower", "2247JD0K")).andExpect(status().isOk())
                .andDo(print()).andReturn();
    }

    @Test
    void lower_error() throws Exception {
        given(this.dictionaryService.lower(Mockito.anyString())).willThrow(new RuntimeException());

        mvc.perform(get("/dictionary/{code}/lower", "2247JD0K")).andExpect(status().isNoContent())
                .andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        DictionaryVO dictionaryVO = new DictionaryVO();
        dictionaryVO.setName("gender");
        given(this.dictionaryService.create(Mockito.any(DictionaryDTO.class))).willReturn(dictionaryVO);

        // 构造请求对象
        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("gender");
        dictionaryDTO.setAlias("男");
        dictionaryDTO.setSuperior("23234FJ0");
        dictionaryDTO.setDescription("描述");
        mvc.perform(post("/dictionary").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dictionaryDTO)).with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("gender"))
                .andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.dictionaryService.create(Mockito.any(DictionaryDTO.class))).willThrow(new RuntimeException());

        // 构造请求对象
        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("gender");
        mvc.perform(post("/dictionary").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dictionaryDTO)).with(csrf().asHeader()))
                .andExpect(status().isExpectationFailed())
                .andDo(print()).andReturn();
    }
}