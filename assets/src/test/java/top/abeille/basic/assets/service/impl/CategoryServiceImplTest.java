/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import top.abeille.basic.assets.entity.Category;
import top.abeille.basic.assets.repository.CategoryRepository;
import top.abeille.basic.assets.vo.CategoryVO;
import top.abeille.common.mock.AbstractServiceMock;

import java.util.Optional;

/**
 * description
 *
 * @author liwenqiang 2019/3/28 20:22
 **/
public class CategoryServiceImplTest extends AbstractServiceMock {

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