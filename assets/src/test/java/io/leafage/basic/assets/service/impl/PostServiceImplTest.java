/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 帖子接口测试
 *
 * @author wq li 2019-08-20 22:38
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

    private PostDTO postDTO;

    @BeforeEach
    void init() {
        postDTO = new PostDTO();
        postDTO.setTitle("title");
        postDTO.setContext("content");
        postDTO.setCover("cover");
        postDTO.setTags(Set.of("tag"));
        postDTO.setCategoryId(1L);
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Post> postsPage = new PageImpl<>(List.of(Mockito.mock(Post.class)), pageable, 2L);
        given(postRepository.findAll(PageRequest.of(0, 2, Sort.by("id")))).willReturn(postsPage);

        Page<PostVO> voPage = postsService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        PostVO postVO = postsService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }

    @Test
    void fetch_posts_null() {
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        PostVO postVO = postsService.fetch(Mockito.anyLong());

        Assertions.assertNull(postVO);
    }

    @Test
    void next() {
        given(postRepository.getFirstByIdGreaterThanAndEnabledTrueOrderByIdAsc(Mockito.anyLong())).willReturn(Mockito.mock(Post.class));

        PostVO postVO = postsService.next(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }


    @Test
    void previous() {
        given(postRepository.getFirstByIdLessThanAndEnabledTrueOrderByIdDesc(Mockito.anyLong())).willReturn(Mockito.mock(Post.class));

        PostVO postVO = postsService.previous(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }

    @Test
    void details() {
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        given(categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(Mockito.mock(Category.class)));

        given(postContentRepository.getByPostId(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.details(Mockito.anyLong());

        Assertions.assertNotNull(postVO);
    }

    @Test
    void details_posts_null() {
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        PostVO postVO = postsService.details(Mockito.anyLong());

        Assertions.assertNull(postVO);
    }

    @Test
    void exist() {
        given(postRepository.existsByTitle(Mockito.anyString())).willReturn(true);

        boolean exist = postsService.exist("test");

        Assertions.assertTrue(exist);
    }

    @Test
    void create() {
        given(postRepository.saveAndFlush(Mockito.any(Post.class))).willReturn(Mockito.mock(Post.class));


        given(postContentRepository.getByPostId(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        given(postContentRepository.saveAndFlush(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.create(postDTO);

        verify(postRepository, times(1)).saveAndFlush(Mockito.any(Post.class));
        verify(postContentRepository, times(1)).saveAndFlush(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void modify() {
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        given(postRepository.save(Mockito.any(Post.class))).willReturn(Mockito.mock(Post.class));

        given(postContentRepository.getByPostId(Mockito.anyLong())).willReturn(Mockito.mock(PostContent.class));

        given(postContentRepository.save(Mockito.any(PostContent.class))).willReturn(Mockito.mock(PostContent.class));

        PostVO postVO = postsService.modify(1L, postDTO);

        verify(postRepository, times(1)).save(Mockito.any(Post.class));
        verify(postContentRepository, times(1)).save(Mockito.any(PostContent.class));
        Assertions.assertNotNull(postVO);
    }

    @Test
    void modify_error() {
        given(postRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Post.class)));

        given(postRepository.save(Mockito.any(Post.class))).willThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> postsService.modify(1L, postDTO));
    }

    @Test
    void remove() {
        postsService.remove(Mockito.anyLong());

        verify(postRepository, times(1)).deleteById(Mockito.anyLong());
    }

}
