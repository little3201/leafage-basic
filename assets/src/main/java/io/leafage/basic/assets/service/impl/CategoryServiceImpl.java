/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import io.leafage.basic.assets.vo.CountVO;
import io.leafage.common.basic.AbstractBasicService;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * 话题信息service实现
 *
 * @author liwenqiang 2020/2/13 20:24
 **/
@Service
public class CategoryServiceImpl extends AbstractBasicService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.categoryRepository = categoryRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<CategoryVO> retrieve(int page, int size) {
        return categoryRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> fetch(String code) {
        Asserts.notBlank(code, "code");
        Category info = new Category();
        info.setCode(code);
        return categoryRepository.findByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Flux<CountVO> countPosts(Set<String> codes) {
        return Flux.fromIterable(codes)
                .flatMap(categoryRepository::findByCodeAndEnabledTrue)
                .flatMap(category ->
                        reactiveMongoTemplate.aggregate(Aggregation.newAggregation(Aggregation.group("category_id")
                                .count().as("count")), Posts.class, CountVO.class));
    }

    @Override
    public Mono<CategoryVO> create(CategoryDTO categoryDTO) {
        Category info = new Category();
        BeanUtils.copyProperties(categoryDTO, info);
        info.setCode(this.generateCode());
        return categoryRepository.insert(info).map(this::convertOuter);
    }

    @Override
    public Mono<CategoryVO> modify(String code, CategoryDTO categoryDTO) {
        Asserts.notBlank(code, "code");
        return categoryRepository.findByCodeAndEnabledTrue(code).flatMap(category -> {
            BeanUtils.copyProperties(categoryDTO, category);
            return categoryRepository.save(category).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return categoryRepository.findByCodeAndEnabledTrue(code).flatMap(topic -> categoryRepository.deleteById(topic.getId()));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private CategoryVO convertOuter(Category info) {
        CategoryVO outer = new CategoryVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
