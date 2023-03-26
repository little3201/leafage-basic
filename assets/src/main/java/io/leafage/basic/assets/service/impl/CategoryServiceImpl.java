/*
 *  Copyright 2018-2023 the original author or authors.
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
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;

/**
 * category service impl
 *
 * @author liwenqiang 2020-02-13 20:24
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Mono<Page<CategoryVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<CategoryVO> voFlux = categoryRepository.findAll(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = categoryRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<CategoryVO> fetch(Long id) {
        return categoryRepository.findById(id).flatMap(this::fetchOuter);
    }

    @Override
    public Mono<Boolean> exist(String categoryName) {
        Assert.hasText(categoryName, "category name must not be blank.");
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    public Mono<CategoryVO> create(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        return categoryRepository.save(category).flatMap(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> modify(Long id, CategoryDTO categoryDTO) {
        Assert.notNull(id, "id must not be null.");
        return categoryRepository.findById(id).doOnNext(category ->
                        BeanUtils.copyProperties(categoryDTO, category)).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(categoryRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(Long id) {
        return categoryRepository.deleteById(id);
    }

    /**
     * 对象转换为输出结果对象(单条查询)
     *
     * @param category 信息
     * @return 输出转换后的vo对象
     */
    private Mono<CategoryVO> fetchOuter(Category category) {
        return Mono.just(category).map(c -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            return categoryVO;
        });
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param category 信息
     * @return 输出转换后的vo对象
     */
    private Mono<CategoryVO> convertOuter(Category category) {
        return this.fetchOuter(category)
                .flatMap(categoryVO -> postRepository.countByCategoryId(category.getId())
                        .switchIfEmpty(Mono.just(0L))
                        .map(count -> {
                            categoryVO.setCount(count);
                            return categoryVO;
                        }));
    }

}
