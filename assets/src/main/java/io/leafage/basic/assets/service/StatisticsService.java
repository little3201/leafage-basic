package io.leafage.basic.assets.service;

import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.vo.StatisticsTotalVO;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

/**
 * statistics service
 *
 * @author liwenqiang 2021/5/19 10:54
 **/
public interface StatisticsService {

    /**
     * 分页查询
     *
     * @param page 分页
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<StatisticsVO>> retrieve(int page, int size);

    /**
     * 统计总量
     *
     * @return 结果
     */
    Mono<StatisticsTotalVO> fetch();

    /**
     * 添加
     *
     * @return 记录结果
     */
    Mono<Statistics> create();

    /**
     * 修改
     *
     * @return 记录结果
     */
    Mono<Statistics> modify();

    /**
     * 记录统计量
     *
     * @param today           当日
     * @param statisticsField 统计记录属性名（viewed, likes, comments, downloads）
     * @return 浏览量
     */
    Mono<Statistics> increase(LocalDate today, StatisticsFieldEnum statisticsField);

}
