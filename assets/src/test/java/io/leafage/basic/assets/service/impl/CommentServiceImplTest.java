package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.domain.Comment;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void retrieve() {
        Comment comment = new Comment();
        comment.setContent("评论信息");
        comment.setPostsId(1L);
        comment.setReplier("2112JK01");
        comment.setCountry("china");
        comment.setLocation("北京市区");
        Page<Comment> page = new PageImpl<>(List.of(comment));
        given(this.commentRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(page);

        Page<CommentVO> voPage = commentService.retrieve(0, 2);
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void relation() {
        Post post = new Post();
        post.setId(1L);
        post.setEnabled(true);
        post.setModifier(1L);
        post.setModifyTime(LocalDateTime.now());
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(post);

        Comment comment = new Comment();
        comment.setContent("评论信息");
        comment.setPostsId(post.getId());
        comment.setModifier(post.getModifier());
        comment.setEnabled(post.isEnabled());
        given(this.commentRepository.findByPostsIdAndReplierIsNullAndEnabledTrue(Mockito.anyLong())).willReturn(List.of(comment));

        List<CommentVO> voList = commentService.relation("2112JK02");
        Assertions.assertNotNull(voList);
    }

    @Test
    void relation_empty() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        List<CommentVO> voList = commentService.relation("2112JK02");
        Assertions.assertTrue(voList.isEmpty());
    }

    @Test
    void replies() {
        Comment comment = new Comment();
        comment.setContent("评论信息");
        comment.setPostsId(1L);

        Comment comm = new Comment();
        comm.setContent("评论信息2222");
        comm.setPostsId(1L);
        comm.setReplier(comment.getReplier());
        given(this.commentRepository.findByReplierAndEnabledTrue(Mockito.anyString())).willReturn(List.of(comment, comm));

        List<CommentVO> voList = commentService.replies("2112JK01");
        Assertions.assertNotNull(voList);
    }

    @Test
    void replies_empty() {
        List<CommentVO> voList = commentService.replies("2112JK01");
        Assertions.assertTrue(voList.isEmpty());
    }

    @Test
    void create_posts_null() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("评论信息");
        commentDTO.setPosts("2112JK02");
        CommentVO commentVO = commentService.create(commentDTO);

        Assertions.assertNull(commentVO);
    }

    @Test
    void create() {
        Post post = new Post();
        post.setId(1L);
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(post);

        Comment comment = new Comment();
        comment.setContent("评论信息");
        comment.setPostsId(post.getId());
        comment.setReplier("2112JK01");
        given(this.commentRepository.saveAndFlush(Mockito.any(Comment.class))).willReturn(comment);

        this.postRepository.increaseComment(post.getId());

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("评论信息");
        commentDTO.setPosts("2112JK02");
        CommentVO commentVO = commentService.create(commentDTO);

        verify(this.commentRepository, times(1)).saveAndFlush(Mockito.any(Comment.class));
        Assertions.assertNotNull(commentVO);
    }
}