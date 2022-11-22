/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.dto.CategoriesDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoriesVO;
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
 * @author liwenqiang 2020/2/13 20:24
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
    public Mono<Page<CategoriesVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<CategoriesVO> voFlux = categoryRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = categoryRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<CategoriesVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return categoryRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter);
    }

    @Override
    public Mono<CategoriesVO> create(CategoriesDTO categoriesDTO) {
        Category info = new Category();
        BeanUtils.copyProperties(categoriesDTO, info);
        info.setCode(this.generateCode());
        return categoryRepository.insert(info).flatMap(this::convertOuter);
    }

    @Override
    public Mono<CategoriesVO> modify(String code, CategoriesDTO categoriesDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return categoryRepository.getByCodeAndEnabledTrue(code).doOnNext(category ->
                        BeanUtils.copyProperties(categoriesDTO, category)).switchIfEmpty(Mono.error(NotContextException::new))
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
    private Mono<CategoriesVO> fetchOuter(Category category) {
        return Mono.just(category).map(c -> {
            CategoriesVO categoriesVO = new CategoriesVO();
            BeanUtils.copyProperties(category, categoriesVO);
            return categoriesVO;
        });
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param category 信息
     * @return 输出转换后的vo对象
     */
    private Mono<CategoriesVO> convertOuter(Category category) {
        return Mono.just(category).map(c -> {
            CategoriesVO categoriesVO = new CategoriesVO();
            BeanUtils.copyProperties(c, categoriesVO);
            return categoriesVO;
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
