package io.leafage.basic.assets.service;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.vo.StatisticsVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 统计信息 service
 *
 * @author liwenqiang 2021/5/19 10:54
 **/
public interface StatisticsService {

    /**
     * 按照分页进行查询并排序
     *
     * @param page 分页
     * @param size 大小
     * @return 结果集
     */
    Flux<StatisticsVO> retrieve(int page, int size);

    /**
     * 查询昨日浏览量统计
     *
     * @return 统计结果
     */
    Mono<StatisticsVO> over();

    /**
     * 浏览量记录
     *
     * @return 记录结果
     */
    Mono<Statistics> create();
}
