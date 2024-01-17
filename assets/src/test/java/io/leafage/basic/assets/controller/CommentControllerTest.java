package io.leafage.basic.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Comment 接口测试
 *
 * @author liwenqiang 2021/12/7 17:54
 **/
@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    private CommentVO commentVO;

    private CommentDTO commentDTO;

    @BeforeEach
    void init() {
        commentVO = new CommentVO();
        commentVO.setContent("评论信息");
        commentVO.setPosts("21389KO6");

        commentDTO = new CommentDTO();
        commentDTO.setPosts("21389KO6");
        commentDTO.setContent("评论了内容");
        commentDTO.setReplier("21389KO1");
    }

    @Test
    void retrieve() throws Exception {
        Page<CommentVO> page = new PageImpl<>(List.of(commentVO));
        given(this.commentService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willReturn(page);

        mvc.perform(get("/comment").queryParam("page", "0").queryParam("size", "2"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Test
    void retrieve_error() throws Exception {
        given(this.commentService.retrieve(Mockito.anyInt(), Mockito.anyInt())).willThrow(new NoSuchElementException());

        mvc.perform(get("/comment").queryParam("page", "0").queryParam("size", "2"))
                .andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void relation() throws Exception {
        given(this.commentService.relation(Mockito.anyLong())).willReturn(List.of(commentVO));

        mvc.perform(get("/comment/{id}", 1L)).andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Test
    void relation_error() throws Exception {
        given(this.commentService.relation(Mockito.anyLong())).willThrow(new NoSuchElementException());

        mvc.perform(get("/comment/{id}", 1L)).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void replies() throws Exception {
        given(this.commentService.replies(Mockito.anyLong())).willReturn(List.of(commentVO));

        mvc.perform(get("/comment/{id}/replies", 1L)).andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Test
    void replies_error() throws Exception {
        given(this.commentService.replies(Mockito.anyLong())).willThrow(new NoSuchElementException());

        mvc.perform(get("/comment/{id}/replies", 1L)).andExpect(status().isNoContent()).andDo(print()).andReturn();
    }

    @Test
    void create() throws Exception {
        given(this.commentService.create(Mockito.any(CommentDTO.class))).willReturn(commentVO);

        mvc.perform(post("/comment").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDTO)).with(csrf().asHeader())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("评论信息")).andDo(print()).andReturn();
    }

    @Test
    void create_error() throws Exception {
        given(this.commentService.create(Mockito.any(CommentDTO.class))).willThrow(new NoSuchElementException());

        mvc.perform(post("/comment").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentDTO)).with(csrf().asHeader())).andExpect(status()
                .isExpectationFailed()).andDo(print()).andReturn();
    }
}