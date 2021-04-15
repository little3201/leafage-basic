/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.entity.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.vo.CategoryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

/**
 * 类目接口测试
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl accountService;

    @Test
    public void fetch() {
        Mockito.when(categoryRepository.findOne(Mockito.any())).thenReturn(Optional.of(Mockito.mock(Category.class)));
        CategoryVO categoryVO = accountService.fetch(Mockito.anyString());
        Assertions.assertNotNull(categoryVO);
    }
}