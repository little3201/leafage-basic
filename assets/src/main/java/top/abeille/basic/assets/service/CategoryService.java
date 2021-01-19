/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import org.springframework.data.domain.Page;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.vo.CategoryVO;
import top.abeille.basic.assets.vo.CountVO;
import top.abeille.common.basic.BasicService;

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
    Page<CategoryVO> retrieves(int page, int size);

    /**
     * 统计
     *
     * @param codes 代码集合
     * @return 数量
     */
    List<CountVO> countByCategory(Set<String> codes);

}
