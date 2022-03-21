package io.leafage.basic.assets.service.impl;

import com.mongodb.client.result.UpdateResult;
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
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * comment service test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void retrieve() {
        Comment comment = new Comment();
        comment.setPostsId(new ObjectId());
        comment.setContent("这里写内容");
        comment.setCountry("某国");
        comment.setLocation("某地");
        given(this.commentRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "modifyTime")))).willReturn(Flux.just(comment));

        given(this.postsRepository.findById(comment.getPostsId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        StepVerifier.create(commentService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        Comment comment = new Comment();
        comment.setPostsId(posts.getId());
        given(this.commentRepository.findByPostsIdAndEnabledTrue(posts.getId())).willReturn(Flux.just(comment));

        given(this.postsRepository.findById(posts.getId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        StepVerifier.create(commentService.relation("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void repliers() {
        Comment comment = new Comment();
        comment.setPostsId(new ObjectId());
        comment.setReplier("21318H9F0");
        given(this.commentRepository.findByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Flux.just(comment));

        Posts posts = new Posts();
        posts.setId(comment.getPostsId());
        given(this.postsRepository.findById(posts.getId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

        StepVerifier.create(commentService.replies("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        Comment comment = new Comment();
        comment.setContent("test");
        comment.setPostsId(posts.getId());
        comment.setReplier("21318H9F0");
        given(this.commentRepository.insert(Mockito.any(Comment.class))).willReturn(Mono.just(comment));

        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(comment.getPostsId())),
                new Update().inc("comment", 1), Posts.class))
                .willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        given(this.postsRepository.findById(comment.getPostsId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

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
        comment.setReplier("21318H9F0");
        comment.setContent("这里写内容");
        given(this.commentRepository.save(Mockito.any(Comment.class))).willReturn(Mono.just(comment));

        given(this.postsRepository.findById(comment.getPostsId())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("测试");
        commentDTO.setContent(comment.getContent());
        StepVerifier.create(commentService.modify("21318H9FH", commentDTO))
                .expectNextCount(1).verifyComplete();
    }
}