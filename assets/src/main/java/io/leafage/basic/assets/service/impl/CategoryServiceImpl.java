/*
 * Copyright (c) 2021. Leafage All Right Reserved.
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * category service impl.
 *
 * @author liwenqiang  2020-12-03 22:59
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
    public Page<CategoryVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "lastModifiedDate"));
        return categoryRepository.findAll(pageable).map(category -> {
            CategoryVO categoryVO = this.convertOuter(category);
            long count = postRepository.countByCategoryId(category.getId());
            categoryVO.setCount(count);
            return categoryVO;
        });
    }

    @Override
    public CategoryVO fetch(Long id) {
        Assert.notNull(id, "category id must not be null.");
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        return this.convertOuter(category);
    }

    @Override
    public boolean exist(String name) {
        Assert.hasText(name, "category name must not be blank.");
        return categoryRepository.existsByName(name);
    }

    @Override
    public CategoryVO create(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category = categoryRepository.saveAndFlush(category);
        return this.convertOuter(category);
    }

    @Override
    public CategoryVO modify(Long id, CategoryDTO categoryDTO) {
        Assert.notNull(id, "category id must not be null.");
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        BeanUtils.copyProperties(categoryDTO, category);
        category = categoryRepository.save(category);
        return this.convertOuter(category);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "category id must not be null.");

        categoryRepository.deleteById(id);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private CategoryVO convertOuter(Category info) {
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(info, vo);

        long count = postRepository.countByCategoryId(info.getId());
        vo.setCount(count);
        return vo;
    }
}
