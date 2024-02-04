package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Comment;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import io.leafage.basic.assets.vo.CommentVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

/**
 * comment 接口测试
 *
 * @author liwenqiang 2021/12/7 17:55
 **/
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostStatisticsRepository postStatisticsRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Comment> page = new PageImpl<>(List.of(Mockito.mock(Comment.class)), pageable, 2L);
        given(this.commentRepository.findAll(PageRequest.of(0, 2))).willReturn(page);

        Page<CommentVO> voPage = commentService.retrieve(0, 2);
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void relation() {
        given(this.commentRepository.findByPostIdAndReplierIsNull(Mockito.anyLong())).willReturn(Mockito.anyList());

        List<CommentVO> voList = commentService.relation(1L);
        Assertions.assertNotNull(voList);
    }

    @Test
    void relation_empty() {
        given(this.commentRepository.findByPostIdAndReplierIsNull(Mockito.anyLong())).willReturn(Collections.emptyList());

        List<CommentVO> voList = commentService.relation(Mockito.anyLong());
        Assertions.assertTrue(voList.isEmpty());
    }

    @Test
    void replies() {
        Comment comment = new Comment();
        comment.setContent("评论信息");
        comment.setPostId(1L);

        Comment comm = new Comment();
        comm.setContent("评论信息2222");
        comm.setPostId(1L);
        comm.setReplier(comment.getReplier());
        given(this.commentRepository.findByReplier(Mockito.anyLong())).willReturn(List.of(comment, comm));

        List<CommentVO> voList = commentService.replies(Mockito.anyLong());
        Assertions.assertNotNull(voList);
    }

    @Test
    void replies_empty() {
        List<CommentVO> voList = commentService.replies(Mockito.anyLong());
        Assertions.assertTrue(voList.isEmpty());
    }

    @Test
    void create() {
        given(this.commentRepository.saveAndFlush(Mockito.any(Comment.class))).willReturn(Mockito.mock(Comment.class));

        doNothing().when(this.postStatisticsRepository).increaseComment(Mockito.anyLong());

        given(this.commentRepository.countByReplier(Mockito.anyLong())).willReturn(2L);

        CommentVO commentVO = commentService.create(Mockito.mock(CommentDTO.class));
        Assertions.assertNotNull(commentVO);
    }

}