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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void retrieve() {
        List<Posts> postsList = new ArrayList<>(2);
        given(this.postsRepository.findAll()).willReturn(postsList);
        List<PostsVO> postsVOS = postsService.retrieve();
        Assertions.assertNotNull(postsVOS);
    }

    @Test
    void retrieve_page() {
        List<Posts> voList = new ArrayList<>(2);
        Page<Posts> postsPage = new PageImpl<>(voList);
        given(this.postsRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(postsPage);
        Page<PostsVO> voPage = postsService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage);
    }

    @Test
    void fetch() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));
        PostsVO postsVO = postsService.fetch("2112JK02");
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void fetchDetails() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));
        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Category.class)));
        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostsContent.class));
        PostsVO postsVO = postsService.fetchDetails("2112JK02");
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void create() {
        given(this.postsRepository.save(Mockito.any(Posts.class))).willReturn(Mockito.mock(Posts.class));
        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostsContent.class));
        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Category.class)));
        PostsVO postsVO = postsService.create(Mockito.mock(PostsDTO.class));
        verify(this.postsRepository, times(1)).save(Mockito.any(Posts.class));
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void modify() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));
        given(this.postsRepository.saveAndFlush(Mockito.any(Posts.class))).willReturn(Mockito.mock(Posts.class));
        given(this.postsContentRepository.findByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostsContent.class));
        given(this.postsContentRepository.saveAndFlush(Mockito.any(PostsContent.class))).willReturn(Mockito.mock(PostsContent.class));
        PostsVO postsVO = postsService.modify("2112JK02", Mockito.mock(PostsDTO.class));
        verify(this.postsRepository, times(1)).saveAndFlush(Mockito.any(Posts.class));
        verify(this.postsContentRepository, times(1)).saveAndFlush(Mockito.any(PostsContent.class));
        Assertions.assertNotNull(postsVO);
    }

    @Test
    void remove() {
        given(this.postsRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Posts.class));
        postsService.remove("2112JK02");
        verify(this.postsRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void flushViewed() {
        postsService.flushViewed("2112JK02");
        verify(this.postsRepository, times(1)).flushViewed(Mockito.anyString());
    }

}
