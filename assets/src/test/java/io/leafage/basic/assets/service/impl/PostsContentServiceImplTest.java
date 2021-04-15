/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.repository.PostsContentRepository;
import io.leafage.basic.assets.service.AbstractMockTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

/**
 * 内容service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
class PostsContentServiceImplTest extends AbstractMockTest {

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