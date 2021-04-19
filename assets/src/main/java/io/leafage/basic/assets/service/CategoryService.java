/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.vo.CategoryVO;
import io.leafage.basic.assets.vo.CountVO;
import io.leafage.common.servlet.BasicService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

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

    /**
     * 统计
     *
     * @param codes 代码集合
     * @return 数量
     */
    List<CountVO> countByCategory(Set<String> codes);

}
