package io.leafage.basic.assets.service;

import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.dto.StatisticsDTO;
import io.leafage.basic.assets.vo.StatisticsVO;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

import java.time.LocalDate;

/**
 * statistics service
 *
 * @author liwenqiang 2021-05-19 10:54
 **/
public interface StatisticsService extends ReactiveBasicService<StatisticsDTO, StatisticsVO, String> {

    /**
     * 记录统计量
     *
     * @param today           当日
     * @param statisticsField 统计记录属性名（viewed, likes, comments, downloads）
     * @return 浏览量
     */
    Mono<Statistics> increase(LocalDate today, StatisticsFieldEnum statisticsField);

}
