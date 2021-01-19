/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.entity.Category;
import top.abeille.basic.assets.repository.CategoryRepository;
import top.abeille.basic.assets.repository.PostsRepository;
import top.abeille.basic.assets.service.CategoryService;
import top.abeille.basic.assets.vo.CategoryVO;
import top.abeille.basic.assets.vo.CountVO;
import top.abeille.common.basic.AbstractBasicService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类Service实现
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Service
public class CategoryServiceImpl extends AbstractBasicService implements CategoryService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final PostsRepository postsRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, PostsRepository postsRepository) {
        this.categoryRepository = categoryRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public Page<CategoryVO> retrieves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public List<CountVO> countByCategory(Set<String> codes) {
        List<Category> ids = categoryRepository.findByCodeInAndEnabledTrue(codes);
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return ids.stream().map(category -> {
            int count = postsRepository.countByCategoryId(category.getId());
            CountVO countVO = new CountVO();
            countVO.setCode(category.getCode());
            countVO.setCount(count);
            return countVO;
        }).collect(Collectors.toList());
    }

    @Override
    public CategoryVO create(CategoryDTO categoryDTO) {
        Category info = new Category();
        BeanUtils.copyProperties(categoryDTO, info);
        info.setCode(this.generateCode());
        categoryRepository.save(info);
        return this.convertOuter(info);
    }

    @Override
    public void remove(String code) {
        Category category = categoryRepository.findByCodeAndEnabledTrue(code);
        if (category != null) {
            log.info("删除code为：{}的账户", code);
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
