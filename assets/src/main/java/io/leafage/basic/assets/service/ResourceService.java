/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.ResourceBO;
import io.leafage.basic.assets.vo.ResourceVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * resource service
 *
 * @author liwenqiang 2020/2/24 11:59
 **/
public interface ResourceService extends ReactiveBasicService<ResourceBO, ResourceVO, String> {

    /**
     * 分页查询
     *
     * @param page     页码
     * @param size     大小
     * @param sort     排序
     * @param category 分类
     * @return 结果集
     */
    Mono<Page<ResourceVO>> retrieve(int page, int size, String sort, String category);

}
