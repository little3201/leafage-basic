/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.repository.PostsContentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

/**
 * 内容service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class PostsContentServiceImplTest {

    @Mock
    private PostsContentRepository postsContentRepository;

    @InjectMocks
    private PostsContentServiceImpl postsContentService;

    @Test
    public void create() {
        Mockito.when(postsContentRepository.save(Mockito.mock(PostsContent.class)))
                .thenReturn(Mono.just(Mockito.mock(PostsContent.class)));
        postsContentService.create(Mockito.mock(PostsContent.class));
        Mockito.verify(postsContentRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }
}