/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.vo.ArticleDetailsVO;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.common.basic.BasicService;

/**
 * 文章信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface ArticleService extends BasicService<ArticleDTO, ArticleVO> {

    /**
     * 根据业务id查询详细信息
     *
     * @param businessId 业务id
     * @return 详细信息
     */
    Mono<ArticleDetailsVO> fetchDetailsByBusinessId(String businessId);

    Flux<Object> fetchCount();

    Flux<ArticleVO> fetchTop10();
}
