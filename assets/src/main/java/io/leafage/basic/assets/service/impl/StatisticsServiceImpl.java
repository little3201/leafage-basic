package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.dto.StatisticsDTO;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * statistics service impl
 *
 * @author liwenqiang 2021-05-19 10:54
 **/
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final String DATE = "date";

    private final StatisticsRepository statisticsRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public StatisticsServiceImpl(StatisticsRepository statisticsRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.statisticsRepository = statisticsRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<StatisticsVO> create(StatisticsDTO statisticsDTO) {
        Statistics statistics = new Statistics();
        BeanUtils.copyProperties(statisticsDTO, statistics);
        // 统计昨天数据，前天的数据，做差值，计算环比数据
        return statisticsRepository.insert(statistics).map(this::convertOuter);
    }

    @Override
    public Mono<Statistics> increase(LocalDate today, StatisticsFieldEnum statisticsFieldEnum) {
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where(DATE).is(today)),
                        new Update().inc(statisticsFieldEnum.value, 1), Statistics.class)
                .flatMap(updateResult -> statisticsRepository.getByModifyTime(today));
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param info 信息
     * @return 输出转换后的vo对象
     */
    private StatisticsVO convertOuter(Statistics info) {
        StatisticsVO outer = new StatisticsVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }

}
