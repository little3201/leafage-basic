package io.leafage.basic.assets.service;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

/**
 * statistics service
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
    Mono<Page<StatisticsVO>> retrieve(int page, int size);

    /**
     * 浏览量记录
     *
     * @return 记录结果
     */
    Mono<Statistics> create();
}
