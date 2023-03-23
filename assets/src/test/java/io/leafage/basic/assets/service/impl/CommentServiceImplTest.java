/*
 *  Copyright 2018-2023 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Comment;
import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
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
 * comment service test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private Post post;

    @BeforeEach
    void init() {
        comment = new Comment();
        comment.setCode("21318H9F1");
        comment.setPostId(1L);
        comment.setContent("这里写内容");
        comment.setCountry("某国");
        comment.setLocation("某地");

        post = new Post();
        post.setId(1L);
    }

    @Test
    void retrieve() {
        given(this.commentRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "modifyTime")))).willReturn(Flux.just(comment));

        given(this.postRepository.findById(comment.getPostId())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

        given(this.commentRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(commentService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void relation() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(post));

        given(this.commentRepository.findByPostIdAndReplierIsNullAndEnabledTrue(post.getId())).willReturn(Flux.just(comment));

        given(this.postRepository.findById(post.getId())).willReturn(Mono.just(post));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

        StepVerifier.create(commentService.relation("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void repliers() {
        given(this.commentRepository.findByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Flux.just(comment));

        given(this.postRepository.findById(post.getId())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

        StepVerifier.create(commentService.replies("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(post));

        given(this.commentRepository.save(Mockito.any(Comment.class))).willReturn(Mono.just(comment));

        given(this.postRepository.findById(comment.getPostId())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setPosts("21318H9FH");
        commentDTO.setReplier(comment.getReplier());
        StepVerifier.create(commentService.create(commentDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.commentRepository.getByCodeAndEnabledTrue(Mockito.anyString())).
                willReturn(Mono.just(Mockito.mock(Comment.class)));

        given(this.commentRepository.save(Mockito.any(Comment.class))).willReturn(Mono.just(comment));

        given(this.postRepository.findById(comment.getPostId())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.commentRepository.countByReplierAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(9L));

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("测试");
        commentDTO.setContent(comment.getContent());
        StepVerifier.create(commentService.modify("21318H9FH", commentDTO))
                .expectNextCount(1).verifyComplete();
    }
}