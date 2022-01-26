package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.entity.Comment;
import io.leafage.basic.assets.entity.Posts;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostsRepository;
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
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;

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
    private PostsRepository postsRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void retrieve() {
        Comment comment = new Comment();
        comment.setNickname("评论人");
        Page<Comment> page = new PageImpl<>(List.of(comment));
        given(this.commentRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(page);

        Page<CommentVO> voPage = commentService.retrieve(0, 2);
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void posts() {
        Posts posts = new Posts();
        posts.setId(1L);
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(posts);

        Comment comment = new Comment();
        comment.setContent("评论信息");
        comment.setPostsId(posts.getId());
        given(this.commentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(List.of(comment));

        List<CommentVO> voList = commentService.posts("2112JK02");
        Assertions.assertNotNull(voList);
    }

    @Test
    void posts_empty() {
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(null);

        List<CommentVO> voList = commentService.posts("2112JK02");
        Assertions.assertTrue(voList.isEmpty());
    }

    @Test
    void create() {
        Comment comment = new Comment();
        comment.setPostsId(1L);
        comment.setNickname("评论人");
        given(this.commentRepository.saveAndFlush(Mockito.any(Comment.class))).willReturn(comment);

        Posts posts = new Posts();
        posts.setId(comment.getPostsId());
        given(this.postsRepository.findById(Mockito.anyLong())).willReturn(Optional.of(posts));

        this.postsRepository.increaseComment(Mockito.anyString());

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setNickname("评论人");
        commentDTO.setEmail("test@leafage.top");
        commentDTO.setContent("评论信息");
        commentDTO.setPosts("2112JK02");
        CommentVO commentVO = commentService.create(commentDTO);
        Assertions.assertNotNull(commentVO);
    }
}