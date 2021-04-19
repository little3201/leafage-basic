/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import io.leafage.basic.assets.entity.Posts;
import io.leafage.basic.assets.repository.PostsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 帖子接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@SpringBootTest
public class PostsServiceImplTest {

    @Mock
    private PostsRepository postsRepository;

    @Test
    public void save() {
        Mockito.when(postsRepository.save(Mockito.any())).thenAnswer(Mockito.mock(Answers.class));
        Posts result = postsRepository.save(Mockito.mock(Posts.class));
        Mockito.verify(postsRepository, Mockito.atMostOnce()).save(Mockito.mock(Posts.class));
        Assertions.assertNotNull(result.getId());
    }

}
