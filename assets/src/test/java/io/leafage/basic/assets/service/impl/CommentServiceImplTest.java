package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Comment;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.BDDMockito.given;

/**
 * comment service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
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
        comment.setPostsId(new ObjectId());
        given(this.commentRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id"))))
                .willReturn(Flux.just(comment));

        given(this.postsRepository.findById(comment.getPostsId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        StepVerifier.create(commentService.retrieve(0, 2, "id"))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void posts() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        Comment comment = new Comment();
        comment.setPostsId(posts.getId());
        given(this.commentRepository.findByPostsIdAndEnabledTrue(posts.getId())).willReturn(Flux.just(comment));

        given(this.postsRepository.findById(posts.getId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        StepVerifier.create(commentService.posts("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        Comment comment = new Comment();
        comment.setContent("test");
        comment.setPostsId(posts.getId());
        given(this.commentRepository.insert(Mockito.any(Comment.class))).willReturn(Mono.just(comment));

        given(this.postsRepository.findById(comment.getPostsId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPosts("21318H9FH");
        StepVerifier.create(commentService.create(commentDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.commentRepository.getByCodeAndEnabledTrue(Mockito.anyString())).
                willReturn(Mono.just(Mockito.mock(Comment.class)));

        Comment comment = new Comment();
        comment.setPostsId(new ObjectId());
        given(this.commentRepository.save(Mockito.any(Comment.class)))
                .willReturn(Mono.just(comment));

        given(this.postsRepository.findById(comment.getPostsId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("测试");
        StepVerifier.create(commentService.modify("21318H9FH", commentDTO))
                .expectNextCount(1).verifyComplete();
    }
}