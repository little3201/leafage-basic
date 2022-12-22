/*
 *  Copyright 2018-2022 the original author or authors.
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
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.PostsContentService;
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
class PostsServiceImplTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostsContentService postsContentService;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    void retrieve() {
        Posts posts = new Posts();
        ObjectId categoryId = new ObjectId();
        posts.setCategoryId(categoryId);
        given(this.postsRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(categoryId)).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postsRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.postsService.retrieve(0, 2, "id", "")).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_category() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(category.getId());
        given(this.postsRepository.findByCategoryIdAndEnabledTrue(category.getId(), PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postsRepository.countByCategoryIdAndEnabledTrue(Mockito.any())).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.postsService.retrieve(0, 2, "id", "21213G0J2"))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(posts));

        given(this.categoryRepository.findById(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.postsContentService.fetchByPostsId(Mockito.any(ObjectId.class))).willReturn(Mono.just(Mockito.mock(PostsContent.class)));


        StepVerifier.create(this.postsService.fetch("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch_empty() {
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.empty());
        StepVerifier.create(this.postsService.fetch("21213G0J2")).verifyError();
    }

    @Test
    void exist() {
        given(this.postsRepository.existsByTitle(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(postsService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));

        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.insert(Mockito.any(Posts.class))).willReturn(Mono.just(posts));

        given(this.postsContentService.create(Mockito.any(PostsContent.class))).willReturn(Mono.empty());

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

        given(this.postsRepository.insert(Mockito.any(Posts.class))).willThrow(new RuntimeException());

        StepVerifier.create(this.postsService.create(postDTO)).verifyError();
    }

    @Test
    void modify() {
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Posts.class)));

        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(category.getId());
        given(this.postsRepository.save(Mockito.any(Posts.class))).willReturn(Mono.just(posts));

        PostsContent postsContent = new PostsContent();
        postsContent.setPostsId(posts.getId());
        given(this.postsContentService.fetchByPostsId(Mockito.any(ObjectId.class)))
                .willReturn(Mono.just(postsContent));

        postsContent.setContent("内容信息");
        given(this.postsContentService.modify(posts.getId(), postsContent))
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
        Posts posts = new Posts();
        ObjectId id = new ObjectId();
        posts.setId(id);
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(posts));

        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(id)),
                Update.update("enabled", false), Posts.class)).willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        StepVerifier.create(postsService.remove("21213G0J2")).verifyComplete();
    }

    @Test
    void next() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(posts));

        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.findByIdGreaterThanAndEnabledTrue(Mockito.any(ObjectId.class),
                Mockito.any(PageRequest.class))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.next("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void previous() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(posts));

        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.findByIdLessThanAndEnabledTrue(Mockito.any(ObjectId.class),
                Mockito.any(PageRequest.class))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.previous("21213G0J2")).expectNextCount(1).verifyComplete();
    }

    @Test
    void search() {
        Posts posts = new Posts();
        posts.setId(new ObjectId());
        posts.setCategoryId(new ObjectId());
        given(this.postsRepository.findAllBy(Mockito.anyString(), Mockito.any(TextCriteria.class))).willReturn(Flux.just(posts));

        given(this.categoryRepository.findById(posts.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(postsService.search("leafage")).expectNextCount(1).verifyComplete();
    }

}