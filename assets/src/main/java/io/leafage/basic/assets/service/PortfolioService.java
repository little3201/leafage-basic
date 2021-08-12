/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.vo.PortfolioVO;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 作品集信息Service
 *
 * @author liwenqiang 2020/2/24 11:59
 **/
public interface PortfolioService extends ReactiveBasicService<PortfolioDTO, PortfolioVO> {

    /**
     * 是否已存在
     *
     * @param title 名称
     * @return true-是，false-否
     */
    Mono<Boolean> exists(String title);
}
