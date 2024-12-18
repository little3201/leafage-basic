/*
 *  Copyright 2018-2024 little3201.
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NotContextException;

/**
 * category service impl
 *
 * @author wq li
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    /**
     * <p>Constructor for CategoryServiceImpl.</p>
     *
     * @param categoryRepository a {@link io.leafage.basic.assets.repository.CategoryRepository} object
     * @param postRepository     a {@link io.leafage.basic.assets.repository.PostRepository} object
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<CategoryVO>> retrieve(int page, int size, String sortBy, boolean descending) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Flux<CategoryVO> voFlux = categoryRepository.findByEnabledTrue(pageable).flatMap(this::convert);

        Mono<Long> count = categoryRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CategoryVO> fetch(Long id) {
        Assert.notNull(id, "id must not be null.");
        return categoryRepository.findById(id).flatMap(this::fetchOuter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> exists(String name) {
        Assert.hasText(name, "name must not be empty.");
        return categoryRepository.existsByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CategoryVO> create(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        return categoryRepository.save(category).flatMap(this::convert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CategoryVO> modify(Long id, CategoryDTO categoryDTO) {
        Assert.notNull(id, "id must not be null.");
        return categoryRepository.findById(id).switchIfEmpty(Mono.error(NotContextException::new))
                .doOnNext(category -> BeanUtils.copyProperties(categoryDTO, category))
                .flatMap(categoryRepository::save)
                .flatMap(this::convert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "id must not be null.");
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
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(c, vo);
            vo.setLastModifiedDate(c.getLastModifiedDate().orElse(null));
            return vo;
        });
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param category 信息
     * @return 输出转换后的vo对象
     */
    private Mono<CategoryVO> convert(Category category) {
        return this.fetchOuter(category).flatMap(vo -> postRepository.countByCategoryId(category.getId())
                .switchIfEmpty(Mono.just(0L))
                .map(count -> {
                    vo.setCount(count);
                    return vo;
                }));
    }

}
