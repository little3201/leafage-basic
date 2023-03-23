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

import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.domain.Category;
import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.service.PostContentService;
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

import java.util.Set;

import static org.mockito.BDDMockito.given;

/**
 * posts service test
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
    private PostContentService postContentService;

    @InjectMocks
    private PostServiceImpl postsService;

    private Post post;
    private Category category;

    @BeforeEach
    void init() {
        category = new Category();
        category.setId(1L);

        post = new Post();
        post.setId(1L);
        post.setCategoryId(category.getId());
    }

    @Test
    void retrieve() {
        given(this.postRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.postsService.retrieve(0, 2, "id", "")).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_category() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        given(this.postRepository.findByCategoryIdAndEnabledTrue(category.getId(), PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postRepository.countByCategoryIdAndEnabledTrue(Mockito.any())).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.postsService.retrieve(0, 2, "id", "21213G0J2"))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(post));

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postContentService.fetchByPostId(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(PostContent.class)));

        StepVerifier.create(this.postsService.fetch("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch_empty() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.empty());
        StepVerifier.create(this.postsService.fetch("21213G0J2")).verifyError();
    }

    @Test
    void exist() {
        given(this.postRepository.existsByTitle(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(postsService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(Mono.just(post));

        given(this.postContentService.create(Mockito.any(PostContent.class))).willReturn(Mono.empty());

        PostDTO postDTO = new PostDTO();
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContext("内容信息");
        postDTO.setContent(contentBO);
        StepVerifier.create(this.postsService.create(postDTO)).verifyComplete();
    }

    @Test
    void create_error_null() {
        PostDTO postDTO = new PostDTO();
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.empty());

        StepVerifier.create(this.postsService.create(postDTO)).verifyError();
    }

    @Test
    void create_error() {
        PostDTO postDTO = new PostDTO();
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postRepository.save(Mockito.any(Post.class))).willThrow(new RuntimeException());

        StepVerifier.create(this.postsService.create(postDTO)).verifyError();
    }

    @Test
    void modify() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Post.class)));

        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(Mono.just(post));

        PostContent postContent = new PostContent();
        postContent.setPostId(post.getId());
        given(this.postContentService.fetchByPostId(Mockito.anyLong()))
                .willReturn(Mono.just(postContent));

        postContent.setContext("内容信息");
        given(this.postContentService.modify(post.getId(), postContent))
                .willReturn(Mono.empty());

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("标题");
        postDTO.setTags(Set.of("test"));
        postDTO.setCover("./avatar.jpg");

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContext("内容信息");
        postDTO.setContent(contentBO);
        StepVerifier.create(this.postsService.modify("21213G0J2", postDTO)).verifyComplete();
    }

    @Test
    void remove() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(post));

        given(this.postRepository.deleteById(Mockito.anyLong())).willReturn(Mono.empty());

        StepVerifier.create(postsService.remove("21213G0J2")).verifyComplete();
    }

    @Test
    void next() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(post));

        given(this.postRepository.findFirstByIdGreaterThanAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.next("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void previous() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(post));

        given(this.postRepository.findByIdLessThanAndEnabledTrue(Mockito.anyLong(),
                Mockito.any(PageRequest.class))).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.previous("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void search() {
        given(this.postRepository.findAllBy(Mockito.anyString())).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.search("leafage")).expectNextCount(1).verifyComplete();
    }

}