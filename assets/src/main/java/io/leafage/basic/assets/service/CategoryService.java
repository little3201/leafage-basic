/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.vo.CategoryVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.BasicService;

/**
 * 分类Service
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
public interface CategoryService extends BasicService<CategoryDTO, CategoryVO> {

    /**
     * 分页查询
     *
     * @param page 查询起点
     * @param size 大小
     * @return 查询结果
     */
    Page<CategoryVO> retrieve(int page, int size, String order);
}
