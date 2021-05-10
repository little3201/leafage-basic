/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.entity.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
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

import static org.mockito.BDDMockito.given;

/**
 * 类目接口测试
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void retrieve() {
        Page<Category> categoryPage = new PageImpl<>(new ArrayList<>(2));
        given(categoryRepository.findAll(Mockito.any(PageRequest.class))).willReturn(categoryPage);
        Page<CategoryVO> voPage = categoryService.retrieve(0, 2, "id");
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(categoryRepository.findByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Category.class));
        CategoryVO categoryVO = categoryService.fetch("21319IDJ0");
        Assertions.assertNotNull(categoryVO);
    }
}