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
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostRepository;
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
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void init() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        categoryDTO.setDescription("category");
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Category> page = new PageImpl<>(List.of(Mockito.mock(Category.class)), pageable, 2L);
        given(categoryRepository.findAll(Mockito.any(PageRequest.class))).willReturn(page);

        given(postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());

        Page<CategoryVO> voPage = categoryService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Category.class)));

        CategoryVO categoryVO = categoryService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(categoryVO);
    }


    @Test
    void create() {
        given(categoryRepository.saveAndFlush(Mockito.any(Category.class))).willReturn(Mockito.mock(Category.class));

        given(postRepository.countByCategoryId(Mockito.anyLong())).willReturn(2L);

        CategoryVO categoryVO = categoryService.create(Mockito.mock(CategoryDTO.class));

        verify(categoryRepository, times(1)).saveAndFlush(Mockito.any(Category.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void modify() {
        given(categoryRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Category.class)));

        given(categoryRepository.save(Mockito.any(Category.class))).willReturn(Mockito.mock(Category.class));

        given(postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());

        CategoryVO categoryVO = categoryService.modify(1L, categoryDTO);

        verify(categoryRepository, times(1)).save(Mockito.any(Category.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void remove() {
        categoryService.remove(Mockito.anyLong());

        verify(categoryRepository, times(1)).deleteById(Mockito.anyLong());
    }
}