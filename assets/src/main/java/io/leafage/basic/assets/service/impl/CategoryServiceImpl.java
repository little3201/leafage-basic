/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.entity.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;

/**
 * category service impl.
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Service
public class CategoryServiceImpl extends AbstractBasicService implements CategoryService {

    private static final String MESSAGE = "code is blank.";

    private final CategoryRepository categoryRepository;
    private final PostsRepository postsRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, PostsRepository postsRepository) {
        this.categoryRepository = categoryRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public Page<CategoryVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "modifyTime"));
        return categoryRepository.findByEnabledTrue(pageable).map(category -> {
            CategoryVO categoryVO = this.convertOuter(category);
            long count = postsRepository.countByCategoryId(category.getId());
            categoryVO.setCount(count);
            return categoryVO;
        });
    }

    @Override
    public CategoryVO fetch(String code) {
        Assert.hasText(code, MESSAGE);
        Category category = categoryRepository.findByCodeAndEnabledTrue(code);
        return this.convertOuter(category);
    }

    @Override
    public CategoryVO create(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setCode(this.generateCode());
        categoryRepository.save(category);
        return this.convertOuter(category);
    }

    @Override
    public CategoryVO modify(String code, CategoryDTO categoryDTO) {
        Assert.hasText(code, MESSAGE);
        Category category = categoryRepository.findByCodeAndEnabledTrue(code);
        BeanUtils.copyProperties(categoryDTO, category);
        categoryRepository.saveAndFlush(category);
        return this.convertOuter(category);
    }

    @Override
    public void remove(String code) {
        Assert.hasText(code, MESSAGE);
        Category category = categoryRepository.findByCodeAndEnabledTrue(code);
        if (category != null) {
            categoryRepository.deleteById(category.getId());
        }
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
