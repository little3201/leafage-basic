package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.service.AbstractMockTest;
import io.leafage.basic.assets.vo.CategoryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

/**
 * 分类service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
class CategoryServiceImplTest extends AbstractMockTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void fetch() {
        Mockito.when(categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .thenReturn(Mono.just(Mockito.mock(Category.class)));
        Mono<CategoryVO> categoryVOMono = categoryService.fetch("21318H9FH");
        Assertions.assertNotNull(categoryVOMono);
    }
}