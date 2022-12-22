/*
 *  Copyright 2018-2022 the original author or authors.
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

import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Resource;
import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.ResourceRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * resource service test
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    @Test
    void retrieve() {
        Resource resource = new Resource();
        resource.setCategoryId(new ObjectId());
        given(this.resourceRepository.findByEnabledTrue(PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(resource));

        given(this.categoryRepository.findById(resource.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.resourceRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.resourceService.retrieve(0, 2, "id", "")).expectNextCount(1).verifyComplete();
    }

    @Test
    void retrieve_category() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Resource resource = new Resource();
        resource.setCategoryId(new ObjectId());
        given(this.resourceRepository.findByCategoryIdAndEnabledTrue(category.getId(), PageRequest.of(0, 2,
                Sort.by(Sort.Direction.DESC, "id")))).willReturn(Flux.just(resource));

        given(this.categoryRepository.findById(resource.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        given(this.resourceRepository.countByCategoryIdAndEnabledTrue(Mockito.any())).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(this.resourceService.retrieve(0, 2, "id", "21318C001")).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Resource resource = new Resource();
        resource.setCategoryId(new ObjectId());
        Mockito.when(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString()))
                .thenReturn(Mono.just(resource));

        given(this.categoryRepository.findById(resource.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        StepVerifier.create(resourceService.fetch("21318H9FH")).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.resourceRepository.existsByTitle(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(resourceService.exist("test")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        Resource resource = new Resource();
        resource.setId(new ObjectId());
        resource.setCategoryId(category.getId());
        resource.setType('E');
        resource.setCode("21318H9FH");
        given(this.resourceRepository.insert(Mockito.any(Resource.class))).willReturn(Mono.just(resource));

        given(this.categoryRepository.findById(resource.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setCover("./avatar.jpg");

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21318000");
        resourceDTO.setCategory(categoryBO);
        StepVerifier.create(resourceService.create(resourceDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        Resource resource = new Resource();
        resource.setId(new ObjectId());
        resource.setType('P');
        resource.setCode("21318H9FH");
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(resource));

        Category category = new Category();
        category.setId(new ObjectId());
        given(this.categoryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(category));

        given(this.resourceRepository.save(Mockito.any(Resource.class))).willReturn(Mono.just(Mockito.mock(Resource.class)));

        given(this.categoryRepository.findById(resource.getCategoryId())).willReturn(Mono.just(Mockito.mock(Category.class)));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setTitle("test");
        resourceDTO.setCover("./avatar.jpg");

        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCode("21318000");
        resourceDTO.setCategory(categoryBO);
        StepVerifier.create(resourceService.modify("21318H9FH", resourceDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Resource resource = new Resource();
        resource.setId(new ObjectId());
        given(this.resourceRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(resource));

        given(this.resourceRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(resourceService.remove("21318H9FH")).verifyComplete();
    }
}