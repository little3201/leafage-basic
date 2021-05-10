/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.entity.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.vo.CategoryVO;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 类目接口测试
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void retrieve() {
        List<Category> categories = new ArrayList<>(2);
        Category category = new Category();
        category.setId(1L);
        categories.add(category);
        categories.add(category);
        Page<Category> categoryPage = new PageImpl<>(categories);
        given(this.categoryRepository.findAll(Mockito.any(PageRequest.class))).willReturn(categoryPage);
        given(this.postsRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());
        Page<CategoryVO> voPage = categoryService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.categoryRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));
        CategoryVO categoryVO = categoryService.fetch("21319IDJ0");
        Assertions.assertNotNull(categoryVO);
    }


    @Test
    void create() {
        given(this.categoryRepository.save(Mockito.any(Category.class))).willReturn(Mockito.mock(Category.class));
        CategoryVO categoryVO = categoryService.create(Mockito.mock(CategoryDTO.class));
        verify(this.categoryRepository, times(1)).save(Mockito.any(Category.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void modify() {
        given(this.categoryRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));
        CategoryVO categoryVO = categoryService.modify("2112JK02", Mockito.mock(CategoryDTO.class));
        verify(this.categoryRepository, times(1)).saveAndFlush(Mockito.any(Category.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void remove() {
        given(this.categoryRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));
        categoryService.remove("2112JK02");
        verify(this.categoryRepository, times(1)).deleteById(Mockito.anyLong());
    }
}