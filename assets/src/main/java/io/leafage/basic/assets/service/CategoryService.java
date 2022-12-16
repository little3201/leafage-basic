/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.vo.CategoryVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * category service
 *
 * @author liwenqiang 2020-02-13 20:16
 **/
public interface CategoryService extends ReactiveBasicService<CategoryDTO, CategoryVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<CategoryVO>> retrieve(int page, int size);
}
