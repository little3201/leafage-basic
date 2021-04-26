/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.PostsContentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * 帖子service测试
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
    public void retrieve() {
        StepVerifier.create(postsService.retrieve(0, 2)).expectNextCount(2).verifyComplete();
    }

    @Test
    public void create() {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("21213G0J2");
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));
        given(this.postsRepository.insert(Mockito.any(Posts.class))).willReturn(Mono.just(Mockito.mock(Posts.class)));
        this.postsContentService.create(Mockito.any(PostsContent.class));
        StepVerifier.create(this.postsService.create(postsDTO))
                .expectSubscription().verifyComplete();
    }

    @Test
    public void create_error() {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("21213G0J2");
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.empty());
        StepVerifier.create(this.postsService.create(postsDTO)).verifyError();
    }

    @Test
    public void create_error2() {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("21213G0J2");
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));
        given(this.postsRepository.insert(Mockito.any(Posts.class))).willReturn(Mono.empty());
        StepVerifier.create(this.postsService.create(postsDTO)).verifyError();
    }

    @Test
    public void fetchDetails() {
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Posts.class)));
        given(this.reactiveMongoTemplate.upsert(Mockito.any(Query.class), Mockito.any(Update.class),
                Posts.class)).willReturn(Mono.just(Mockito.mock(UpdateResult.class)));
        StepVerifier.create(this.postsService.fetchDetails("21213G0J2")).verifyComplete();
    }

    @Test
    public void fetchDetails_empty() {
        given(this.postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.empty());
        doThrow(new RuntimeException()).when(this.reactiveMongoTemplate.upsert(Mockito.any(Query.class),
                Mockito.any(Update.class), Posts.class));
        StepVerifier.create(this.postsService.fetchDetails("21213G0J2")).verifyError();
    }
}