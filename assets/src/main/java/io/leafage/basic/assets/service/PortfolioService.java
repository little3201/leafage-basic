/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.vo.PortfolioVO;
import reactor.core.publisher.Flux;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 作品集信息Service
 *
 * @author liwenqiang 2020/2/24 11:59
 **/
public interface PortfolioService extends ReactiveBasicService<PortfolioDTO, PortfolioVO> {

    /**
     * 按照分页和分类进行查询并排序
     *
     * @param page  分页
     * @param size  大小
     * @param order 排序
     * @return 结果集
     */
    Flux<PortfolioVO> retrieve(int page, int size, String order);

}
