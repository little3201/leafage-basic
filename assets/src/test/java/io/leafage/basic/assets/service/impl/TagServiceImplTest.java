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

import io.leafage.basic.assets.domain.Tag;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.repository.TagRepository;
import io.leafage.basic.assets.vo.CategoryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 类目接口测试
 *
 * @author wq li 2019/3/28 20:22
 **/
@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private TagServiceImpl categoryService;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void init() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        categoryDTO.setDescription("tag");
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Tag> page = new PageImpl<>(List.of(Mockito.mock(Tag.class)), pageable, 2L);
        given(tagRepository.findAll(Mockito.any(PageRequest.class))).willReturn(page);

        given(postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());

        Page<CategoryVO> voPage = categoryService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(tagRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Tag.class)));

        CategoryVO categoryVO = categoryService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(categoryVO);
    }


    @Test
    void create() {
        given(tagRepository.saveAndFlush(Mockito.any(Tag.class))).willReturn(Mockito.mock(Tag.class));

        given(postRepository.countByCategoryId(Mockito.anyLong())).willReturn(2L);

        CategoryVO categoryVO = categoryService.create(Mockito.mock(CategoryDTO.class));

        verify(tagRepository, times(1)).saveAndFlush(Mockito.any(Tag.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void modify() {
        given(tagRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Tag.class)));

        given(tagRepository.save(Mockito.any(Tag.class))).willReturn(Mockito.mock(Tag.class));

        given(postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());

        CategoryVO categoryVO = categoryService.modify(1L, categoryDTO);

        verify(tagRepository, times(1)).save(Mockito.any(Tag.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void remove() {
        categoryService.remove(Mockito.anyLong());

        verify(tagRepository, times(1)).deleteById(Mockito.anyLong());
    }
}