package io.leafage.basic.assets.service;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.vo.StatisticsVO;
import reactor.core.publisher.Mono;

/**
 * 统计信息 service
 *
 * @author liwenqiang 2021/5/19 10:54
 **/
public interface StatisticsService {

    /**
     * 浏览量统计
     *
     * @return 统计结果
     */
    Mono<StatisticsVO> viewed();

    /**
     * 浏览量记录
     *
     * @return 记录结果
     */
    Mono<Statistics> create();
}
