/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.domain.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostRepository;
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
    private PostRepository postRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void retrieve() {
        Category category = new Category();
        category.setId(1L);
        Page<Category> page = new PageImpl<>(List.of(category));
        given(this.categoryRepository.findByEnabledTrue(Mockito.any(PageRequest.class))).willReturn(page);

        given(this.postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());

        Page<CategoryVO> voPage = categoryService.retrieve(0, 2, "id");

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));

        CategoryVO categoryVO = categoryService.fetch("21319IDJ0");

        Assertions.assertNotNull(categoryVO);
    }


    @Test
    void create() {
        Category category = new Category();
        category.setId(1L);
        given(this.categoryRepository.saveAndFlush(Mockito.any(Category.class))).willReturn(category);

        given(this.postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        CategoryVO categoryVO = categoryService.create(categoryDTO);

        verify(this.categoryRepository, times(1)).saveAndFlush(Mockito.any(Category.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void modify() {
        Category category = new Category();
        category.setId(1L);
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(category);

        given(this.categoryRepository.save(Mockito.any(Category.class))).willReturn(category);

        given(this.postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mockito.anyLong());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        CategoryVO categoryVO = categoryService.modify("2112JK02", categoryDTO);

        verify(this.categoryRepository, times(1)).save(Mockito.any(Category.class));
        Assertions.assertNotNull(categoryVO);
    }

    @Test
    void remove() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));

        categoryService.remove("2112JK02");

        verify(this.categoryRepository, times(1)).deleteById(Mockito.anyLong());
    }
}