/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.entity.Category;
import io.leafage.basic.assets.entity.Posts;
import io.leafage.basic.assets.entity.PostsContent;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsContentRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.vo.PostsVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

/**
 * 帖子接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@ExtendWith(MockitoExtension.class)
class PostsServiceImplTest {

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private PostsContentRepository postsContentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    void create() {
        given(postsRepository.save(Mockito.any(Posts.class))).willReturn(Mockito.mock(Posts.class));
        given(postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostsContent.class));
        given(categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Category.class)));
        PostsVO postsVO = postsService.create(Mockito.mock(PostsDTO.class));
        Mockito.verify(postsRepository, times(1)).save(Mockito.any(Posts.class));
        Assertions.assertNotNull(postsVO);
    }

}
