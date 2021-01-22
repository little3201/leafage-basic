/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Flux;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.vo.CategoryVO;
import top.abeille.basic.assets.vo.CountVO;
import top.abeille.common.basic.BasicService;

import java.util.Set;

/**
 * 话题信息Service
 *
 * @author liwenqiang 2020/2/13 20:16
 **/
public interface CategoryService extends BasicService<CategoryDTO, CategoryVO> {

    /**
     * 统计关联信息
     *
     * @param codes code集合
     * @return 统计信息
     */
    Flux<CountVO> countPosts(Set<String> codes);
}
