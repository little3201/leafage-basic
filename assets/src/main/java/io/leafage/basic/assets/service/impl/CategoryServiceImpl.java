/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import io.leafage.common.basic.AbstractBasicService;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 话题信息service实现
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
    public Flux<CategoryVO> retrieve() {
        return categoryRepository.findByEnabledTrue().map(this::convertOuter);
    }

    @Override
    public Flux<CategoryVO> retrieve(int page, int size) {
        return categoryRepository.findByEnabledTrue(PageRequest.of(page, size))
                .flatMap(category -> postsRepository.countByCategoryIdAndEnabledTrue(category.getId())
                        .map(count -> {
                            CategoryVO categoryVO = new CategoryVO();
                            BeanUtils.copyProperties(category, categoryVO);
                            categoryVO.setCount(count);
                            return categoryVO;
                        })
                );
    }

    @Override
    public Mono<CategoryVO> fetch(String code) {
        Asserts.notBlank(code, "code");
        Category info = new Category();
        info.setCode(code);
        return categoryRepository.getByCodeAndEnabledTrue(code).flatMap(category -> postsRepository.countByCategoryIdAndEnabledTrue(category.getId())
                .map(count -> {
                    CategoryVO categoryVO = new CategoryVO();
                    BeanUtils.copyProperties(category, categoryVO);
                    categoryVO.setCount(count);
                    return categoryVO;
                })
        );
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
        return categoryRepository.getByCodeAndEnabledTrue(code).flatMap(category -> {
            BeanUtils.copyProperties(categoryDTO, category);
            return categoryRepository.save(category).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> remove(String code) {
        Asserts.notBlank(code, "code");
        return categoryRepository.getByCodeAndEnabledTrue(code).flatMap(topic -> categoryRepository.deleteById(topic.getId()));
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
