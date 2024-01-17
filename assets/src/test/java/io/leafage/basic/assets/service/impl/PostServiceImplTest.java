/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;


import io.leafage.basic.assets.domain.Category;
import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostContentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.vo.PostVO;
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
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostContentRepository postContentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostsServiceImpl postsService;

    @Test
    void retrieve_page() {
        Page<Post> postsPage = new PageImpl<>(List.of(Mockito.mock(Post.class)));
        given(this.postRepository.findByEnabledTrue(PageRequest.of(0, 2, Sort.by("id")))).willReturn(postsPage);

        Page<PostVO> voPage = postsService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        PostVO postVO = postsService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }

    @Test
    void fetch_posts_null() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        PostVO postVO = postsService.fetch(Mockito.anyLong());

        Assertions.assertNull(postVO);
    }

    @Test
    void next() {
        given(this.postRepository.getFirstByIdGreaterThanAndEnabledTrueOrderByIdAsc(Mockito.anyLong())).willReturn(Mockito.mock(Post.class));

        PostVO postVO = postsService.next(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }


    @Test
    void previous() {
        given(this.postRepository.getFirstByIdLessThanAndEnabledTrueOrderByIdDesc(Mockito.anyLong())).willReturn(Mockito.mock(Post.class));

        PostVO postVO = postsService.previous(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }

    @Test
    void details() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Category.class)));

        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.details(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }

    @Test
    void details_posts_null() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        PostVO postVO = postsService.details(Mockito.anyLong());

        Assertions.assertNull(postVO);
    }

    @Test
    void exist() {
        given(this.postRepository.existsByTitle(Mockito.anyString())).willReturn(true);

        boolean exist = postsService.exist("spring");

        Assertions.assertTrue(exist);
    }

    @Test
    void create() {
        given(this.postRepository.saveAndFlush(Mockito.any(Post.class))).willReturn(Mockito.mock(Post.class));

        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        given(this.postContentRepository.saveAndFlush(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.create(Mockito.mock(PostDTO.class));

        verify(this.postRepository, times(1)).saveAndFlush(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).saveAndFlush(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void modify() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(Mockito.mock(Post.class));

        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        given(this.postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.modify(Mockito.anyLong(), Mockito.mock(PostDTO.class));

        verify(this.postRepository, times(1)).save(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).save(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void modify_error() {
        given(this.postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        given(this.postRepository.save(Mockito.any(Post.class))).willReturn(Mockito.any(Post.class));

        given(this.postContentRepository.getByPostsIdAndEnabledTrue(Mockito.anyLong())).willReturn(null);

        given(this.postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.modify(Mockito.anyLong(), Mockito.mock(PostDTO.class));

        verify(this.postRepository, times(1)).save(Mockito.any(Post.class));
        verify(this.postContentRepository, times(1)).save(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void remove() {
        postsService.remove(Mockito.anyLong());

        verify(this.postRepository, times(1)).deleteById(Mockito.anyLong());
    }

}
