/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.entity.Category;
import top.abeille.basic.assets.repository.CategoryRepository;
import top.abeille.basic.assets.service.CategoryService;

import java.util.List;
import java.util.Optional;

/**
 * 分类Service实现
 *
 * @author liwenqiang 2018/12/17 19:27
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void remove(String code) {
        Optional<Category> optional = this.fetchInfo(code);
        if (optional.isPresent()) {
            Category info = optional.get();
            log.info("删除code为：{}的账户", code);
            categoryRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeAll(List<CategoryDTO> entities) {
    }

    /**
     * 根据代码查信息
     *
     * @param code 代码
     * @return 数据库对象信息
     */
    private Optional<Category> fetchInfo(String code) {
        Category category = new Category();
        category.setCode(code);
        return categoryRepository.findOne(Example.of(category));
    }
}
