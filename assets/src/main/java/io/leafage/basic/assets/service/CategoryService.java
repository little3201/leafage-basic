/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.vo.CategoryVO;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 类目信息Service
 *
 * @author liwenqiang 2020/2/13 20:16
 **/
public interface CategoryService extends ReactiveBasicService<CategoryDTO, CategoryVO> {

    /**
     * 是否已存在
     *
     * @param alias 名称
     * @return true-是，false-否
     */
    Mono<Boolean> exists(String alias);
}
