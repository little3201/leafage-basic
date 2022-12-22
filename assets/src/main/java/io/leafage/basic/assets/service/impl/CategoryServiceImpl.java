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

import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
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
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.ValidMessage;

import javax.naming.NotContextException;

/**
 * category service impl
 *
 * @author liwenqiang 2020-02-13 20:24
 **/
@Service
public class CategoryServiceImpl extends AbstractBasicService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostsRepository postsRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, PostsRepository postsRepository) {
        this.categoryRepository = categoryRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public Mono<Page<CategoryVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<CategoryVO> voFlux = categoryRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = categoryRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<CategoryVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return categoryRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter);
    }

    @Override
    public Mono<CategoryVO> create(CategoryDTO categoryDTO) {
        Category info = new Category();
        BeanUtils.copyProperties(categoryDTO, info);
        info.setCode(this.generateCode());
        return categoryRepository.insert(info).flatMap(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> modify(String code, CategoryDTO categoryDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return categoryRepository.getByCodeAndEnabledTrue(code).doOnNext(category ->
                        BeanUtils.copyProperties(categoryDTO, category)).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(categoryRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return categoryRepository.getByCodeAndEnabledTrue(code).flatMap(topic -> categoryRepository.deleteById(topic.getId()));
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
        return Mono.just(category).map(c -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(c, categoryVO);
            return categoryVO;
        }).flatMap(categoryVO -> postsRepository.countByCategoryIdAndEnabledTrue(category.getId())
                .switchIfEmpty(Mono.just(0L))
                .map(count -> {
                    categoryVO.setCount(count);
                    return categoryVO;
                }));
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return categoryRepository.existsByName(name);
    }
}
