/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.vo.CategoryVO;
import io.leafage.basic.assets.vo.CountVO;
import io.leafage.common.basic.BasicService;
import reactor.core.publisher.Flux;

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
