/*
 *  Copyright 2018-2024 the original author or authors.
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * category service test
 *
 * @author liwenqiang 2020-03-01 22:07
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void init() {
        category = new Category();
        category.setId(1L);
        category.setName("test");
    }

    @Test
    void retrieve() {
        given(this.categoryRepository.findByEnabledTrue(Mockito.any(PageRequest.class))).willReturn(Flux.just(category));

        given(this.postRepository.countByCategoryId(Mockito.anyLong())).willReturn(Mono.just(1L));

        given(this.categoryRepository.count()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.categoryService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.categoryRepository.findById(Mockito.anyLong()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(categoryService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.categoryRepository.existsByCategoryName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(categoryService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        given(this.categoryRepository.save(Mockito.any(Category.class))).willReturn(Mono.just(category));

        given(this.postRepository.countByCategoryId(category.getId())).willReturn(Mono.just(2L));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        StepVerifier.create(categoryService.create(categoryDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.categoryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.categoryRepository.save(Mockito.any(Category.class))).willReturn(Mono.just(category));

        given(this.postRepository.countByCategoryId(category.getId())).willReturn(Mono.just(2L));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("test");
        StepVerifier.create(categoryService.modify(1L, categoryDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        given(this.categoryRepository.deleteById(Mockito.anyLong())).willReturn(Mono.empty());

        StepVerifier.create(categoryService.remove(1L)).verifyComplete();
    }
}