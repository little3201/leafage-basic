/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.repository.PostsContentRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

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
        given(this.postsContentRepository.insert(Mockito.any(PostsContent.class)))
                .willReturn(Mono.just(Mockito.mock(PostsContent.class)));
        this.postsContentService.create(Mockito.mock(PostsContent.class));
        Mockito.verify(this.postsContentRepository, times(1)).insert(Mockito.any(PostsContent.class));
    }

    @Test
    public void modify() {
        given(this.postsContentRepository.getByPostsIdAndEnabledTrue(Mockito.any(ObjectId.class)))
                .willReturn(Mono.just(Mockito.mock(PostsContent.class)));
        given(this.postsContentRepository.save(Mockito.any(PostsContent.class)))
                .willReturn(Mono.just(Mockito.mock(PostsContent.class)));
        ObjectId id = new ObjectId();
        this.postsContentService.modify(id, new PostsContent());
        Mockito.verify(this.postsContentRepository, times(1)).save(Mockito.any(PostsContent.class));
    }
}