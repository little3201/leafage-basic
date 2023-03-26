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

    private CommentDTO commentDTO;

    @BeforeEach
    void init() {
        commentDTO = new CommentDTO();
        commentDTO.setPostId(1L);
        commentDTO.setReplier(1L);
    }

    @Test
    void comments() {
        given(this.commentRepository.findByPostIdAndReplierIsNull(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(Comment.class)));

        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.commentRepository.countByReplier(Mockito.anyLong())).willReturn(Mono.just(9L));

        StepVerifier.create(commentService.comments(Mockito.anyLong())).expectNextCount(1).verifyComplete();
    }

    @Test
    void repliers() {
        given(this.commentRepository.findByReplier(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(Comment.class)));

        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.commentRepository.countByReplier(Mockito.anyLong())).willReturn(Mono.just(9L));

        StepVerifier.create(commentService.replies(Mockito.anyLong())).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.commentRepository.save(Mockito.any(Comment.class))).willReturn(Mono.just(Mockito.mock(Comment.class)));

        given(this.commentRepository.countByReplier(Mockito.anyLong())).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(commentService.create(commentDTO)).expectNextCount(1).verifyComplete();
    }

}