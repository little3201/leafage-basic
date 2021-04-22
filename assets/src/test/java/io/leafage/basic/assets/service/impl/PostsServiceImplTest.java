/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.vo.PostsVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

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

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    public void create() {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setCategory("21213G0J2");
        given(categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Category.class)));
        given(postsRepository.insert(Mockito.any(Posts.class))).willReturn(Mono.just(Mockito.mock(Posts.class)));
        postsService.create(postsDTO);
        Mockito.verify(this.postsRepository, times(1)).insert(Mockito.any(Posts.class));
    }

    @Test
    public void create_error() {
        given(this.postsRepository.save(Mockito.mock(Posts.class))).willThrow(new RuntimeException());
        postsService.create(Mockito.mock(PostsDTO.class));
        Mockito.verify(postsRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void fetchDetails() {
        Mockito.when(postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .thenReturn(Mono.just(Mockito.mock(Posts.class)));
        StepVerifier.create(postsService.fetchDetails("21213G0J2")).expectSubscription().verifyComplete();
    }

    @Test
    public void fetchDetails_empty() {
        Mockito.when(postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).thenReturn(Mono.empty());
        Mono<? extends PostsVO> outerMono = postsService.fetchDetails(String.valueOf(new Random().nextFloat()));
        Assertions.assertNull(outerMono);
    }
}