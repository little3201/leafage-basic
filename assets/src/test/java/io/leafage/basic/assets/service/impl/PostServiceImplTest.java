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

import io.leafage.basic.assets.domain.Category;
import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostContentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.BDDMockito.given;

/**
 * post service test
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostContentRepository postContentRepository;

    @InjectMocks
    private PostServiceImpl postsService;

    private PostDTO postDTO;

    @BeforeEach
    void init() {
        postDTO = new PostDTO();
        postDTO.setTitle("标题");
        postDTO.setTags(Set.of("test"));
        postDTO.setCover("./avatar.jpg");
        postDTO.setContext("内容信息");
    }

    @Test
    void retrieve_without_categoryId() {
        given(this.postRepository.findAll(Mockito.any(PageRequest.class))).willReturn(Flux.just(Mockito.mock(Post.class)));

        given(this.postRepository.count()).willReturn(Mono.just(Mockito.anyLong()));

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(this.postsService.retrieve(0, 2, "", null))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_with_categoryId() {
        given(this.postRepository.findByCategoryId(Mockito.anyLong(), Mockito.any(PageRequest.class))).willReturn(Flux.just(Mockito.mock(Post.class)));

        given(this.postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mono.just(1L));

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(this.postsService.retrieve(0, 2, null, 1L))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postContentRepository.getByPostId(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(PostContent.class)));

        StepVerifier.create(this.postsService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.postRepository.existsByTitle(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(postsService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mono.empty());

        StepVerifier.create(this.postsService.create(postDTO)).verifyComplete();
    }

    @Test
    void create_error() {
        given(this.postRepository.save(Mockito.any(Post.class))).willThrow(new RuntimeException());

        StepVerifier.create(this.postsService.create(postDTO)).verifyError();
    }

    @Test
    void modify() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.postContentRepository.getByPostId(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(PostContent.class)));

        given(this.postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mono.empty());

        StepVerifier.create(this.postsService.modify(1L, postDTO)).verifyComplete();
    }

    @Test
    void remove() {
        given(this.postRepository.deleteById(Mockito.anyLong())).willReturn(Mono.empty());

        StepVerifier.create(postsService.remove(1L)).verifyComplete();
    }

    @Test
    void search() {
        given(this.postRepository.findAllByTitle(Mockito.anyString())).willReturn(Flux.just(Mockito.mock(Post.class)));

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.search("test")).expectNextCount(1).verifyComplete();
    }

}