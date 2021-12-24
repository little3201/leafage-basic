/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.ResourceDTO;
import io.leafage.basic.assets.vo.ResourceVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 作品集信息Service
 *
 * @author liwenqiang 2020/2/24 11:59
 **/
public interface ResourceService extends ReactiveBasicService<ResourceDTO, ResourceVO, String> {

    /**
     * 按照分页和分类进行查询并排序
     *
     * @param page     分页
     * @param size     大小
     * @param sort     排序
     * @param category 分类
     * @return 结果集
     */
    Flux<ResourceVO> retrieve(int page, int size, String sort, String category);

    /**
     * 统计¬
     *
     * @param category 类目
     * @return 统计数
     */
    Mono<Long> count(String category);
}
