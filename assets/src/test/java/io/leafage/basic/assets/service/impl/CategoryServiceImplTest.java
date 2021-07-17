/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import org.bson.types.ObjectId;
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
 * category service测试
 *
 * @author liwenqiang 2020/3/1 22:07
 */
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
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.findByEnabledTrue()).willReturn(Flux.just(category));

        given(this.postsRepository.countByCategoryIdAndEnabledTrue(category.getId())).willReturn(Mono.just(2L));

        StepVerifier.create(this.categoryService.retrieve()).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_page() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.findByEnabledTrue(PageRequest.of(0, 2)))
                .willReturn(Flux.just(category));

        given(this.postsRepository.countByCategoryIdAndEnabledTrue(Mockito.any(ObjectId.class))).willReturn(Mono.just(2L));

        StepVerifier.create(this.categoryService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    public void fetch() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(categoryService.fetch("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.categoryRepository.count()).willReturn(Mono.just(2L));
        StepVerifier.create(categoryService.count()).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.insert(Mockito.any(Category.class))).willReturn(Mono.just(category));

        given(this.postsRepository.countByCategoryIdAndEnabledTrue(category.getId())).willReturn(Mono.just(2L));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        StepVerifier.create(categoryService.create(categoryDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(Mockito.mock(Category.class)));

        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.save(Mockito.any(Category.class))).willReturn(Mono.just(category));

        given(this.postsRepository.countByCategoryIdAndEnabledTrue(category.getId())).willReturn(Mono.just(2L));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setAlias("test");
        StepVerifier.create(categoryService.modify("21318H9FH", categoryDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        given(this.categoryRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(categoryService.remove("21318H9FH")).verifyComplete();
    }
}