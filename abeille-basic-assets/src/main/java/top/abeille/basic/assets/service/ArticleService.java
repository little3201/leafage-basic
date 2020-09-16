/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.basic.assets.vo.DetailsVO;
import top.abeille.basic.assets.vo.StatisticsVO;
import top.abeille.common.basic.BasicService;

/**
 * 文章信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface ArticleService extends BasicService<ArticleDTO, ArticleVO> {

    /**
     * 根据代码查询详细信息
     *
     * @param code 代码
     * @return 详细信息
     */
    Mono<DetailsVO> fetchDetailsByCode(String code);

    /**
     * 获取一个月内每日更新数量
     *
     * @return 一个月内每日更新数量
     */
    Flux<StatisticsVO> statistics();

    Flux<ArticleVO> fetchTop10();
}
