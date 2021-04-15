package io.leafage.basic.assets.service.impl;


import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.AbstractMockTest;
import io.leafage.basic.assets.vo.PostsVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.Random;

/**
 * 帖子service测试
 *
 * @author liwenqiang 2019/9/19 9:27
 */
class PostsServiceImplTest extends AbstractMockTest {

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    public void create() {
        postsService.create(Mockito.mock(PostsDTO.class));
        Mockito.verify(postsRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    @Test
    public void create_error() {
        Mockito.when(postsRepository.save(Mockito.mock(Posts.class))).thenThrow(new RuntimeException());
        postsService.create(Mockito.mock(PostsDTO.class));
        Mockito.verify(postsRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void fetchDetails() {
        Mockito.when(postsRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .thenReturn(Mono.just(Mockito.mock(Posts.class)));
        Mono<? extends PostsVO> outerMono = postsService.fetchDetails(Mockito.anyString());
        Assertions.assertNotNull(outerMono);
    }

    @Test
    public void fetchDetails_empty() {
        Mockito.when(postsRepository.getByCodeAndEnabledTrue(Mockito.anyString())).thenReturn(Mono.empty());
        Mono<? extends PostsVO> outerMono = postsService.fetchDetails(String.valueOf(new Random().nextFloat()));
        Assertions.assertNull(outerMono);
    }
}