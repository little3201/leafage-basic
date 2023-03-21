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


import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Post;
import io.leafage.basic.assets.document.PostContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.service.PostContentService;
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
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
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

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private PostServiceImpl postsService;

    @Test
    void retrieve() {
        Post post = new Post();
        ObjectId categoryId = new ObjectId();
        post.setCategoryId(categoryId);
        given(this.postRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(categoryId)).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.postsService.retrieve(0, 2, "id", "")).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_category() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Post post = new Post();
        post.setId(new ObjectId());
        post.setCategoryId(category.getId());
        given(this.postRepository.findByCategoryIdAndEnabledTrue(category.getId(), PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postRepository.countByCategoryIdAndEnabledTrue(Mockito.any())).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.postsService.retrieve(0, 2, "id", "21213G0J2"))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Post post = new Post();
        post.setId(new ObjectId());
        post.setCategoryId(new ObjectId());
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(post));

        given(this.categoryRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postContentService.fetchByPostsId(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(PostContent.class)));

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

        Post post = new Post();
        post.setId(new ObjectId());
        post.setCategoryId(new ObjectId());
        given(this.postRepository.insert(Mockito.any(Post.class))).willReturn(Mono.just(post));

        given(this.postContentService.create(Mockito.any(PostContent.class))).willReturn(Mono.empty());

        PostDTO postDTO = new PostDTO();
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21213G0J2");
        postDTO.setCategory(categoryBO);

        ContentBO contentBO = new ContentBO();
        contentBO.setCatalog("目录");
        contentBO.setContent("内容信息");
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

        given(this.postRepository.insert(Mockito.any(Post.class))).willThrow(new RuntimeException());

        StepVerifier.create(this.postsService.create(postDTO)).verifyError();
    }

    @Test
    void modify() {
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Post.class)));

        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Post post = new Post();
        post.setId(new ObjectId());
        post.setCategoryId(category.getId());
        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(Mono.just(post));

        PostContent postContent = new PostContent();
        postContent.setPostsId(post.getId());
        given(this.postContentService.fetchByPostsId(Mockito.any(ObjectId.class)))
                .willReturn(Mono.just(postContent));

        postContent.setContent("内容信息");
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
        contentBO.setContent("内容信息");
        postDTO.setContent(contentBO);
        StepVerifier.create(this.postsService.modify("21213G0J2", postDTO)).verifyComplete();
    }

    @Test
    void remove() {
        Post post = new Post();
        ObjectId id = new ObjectId();
        post.setId(id);
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(post));

        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(id)),
                Update.update("enabled", false), Post.class)).willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        StepVerifier.create(postsService.remove("21213G0J2")).verifyComplete();
    }

    @Test
    void next() {
        Post post = new Post();
        post.setId(new ObjectId());
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(post));

        post.setCategoryId(new ObjectId());
        given(this.postRepository.findFirstByIdGreaterThanAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.next("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void previous() {
        Post post = new Post();
        post.setId(new ObjectId());
        given(this.postRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(post));

        post.setCategoryId(new ObjectId());
        given(this.postRepository.findByIdLessThanAndEnabledTrue(Mockito.any(ObjectId.class),
                Mockito.any(PageRequest.class))).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.previous("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void search() {
        Post post = new Post();
        post.setId(new ObjectId());
        post.setCategoryId(new ObjectId());
        given(this.postRepository.findAllBy(Mockito.anyString(), Mockito.any(TextCriteria.class))).willReturn(Flux.just(post));

        given(this.categoryRepository.findById(post.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.search("leafage")).expectNextCount(1).verifyComplete();
    }

}