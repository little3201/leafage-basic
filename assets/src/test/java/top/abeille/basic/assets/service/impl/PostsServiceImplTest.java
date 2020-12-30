package top.abeille.basic.assets.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Posts;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.vo.PostsVO;

import java.util.Random;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@SpringBootTest
public class PostsServiceImplTest {

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
    public void createError() {
        Mockito.when(postsRepository.save(Mockito.mock(Posts.class))).thenThrow(new RuntimeException());
        postsService.create(Mockito.mock(PostsDTO.class));
        Mockito.verify(postsRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void fetchDetailsByCode() {
        Mockito.when(postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).thenReturn(Mockito.any());
        Mono<? extends PostsVO> outerMono = postsService.fetchDetailsByCode(Mockito.anyString());
        Assertions.assertNotNull(outerMono);
    }

    @Test
    public void fetchDetailsByCodeEmpty() {
        Mockito.when(postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).thenReturn(Mono.empty());
        Mono<? extends PostsVO> outerMono = postsService.fetchDetailsByCode(String.valueOf(new Random().nextFloat()));
        Assertions.assertNull(outerMono);
    }
}