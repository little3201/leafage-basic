package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * statistics service impl
 *
 * @author liwenqiang 2021/5/19 10:54
 **/
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final PostsRepository postsRepository;
    private final StatisticsRepository statisticsRepository;

    public StatisticsServiceImpl(PostsRepository postsRepository, StatisticsRepository statisticsRepository) {
        this.postsRepository = postsRepository;
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public Flux<StatisticsVO> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifyTime"));
        return statisticsRepository.findByEnabledTrue(pageable).map(this::convertOuter);
    }

    @Override
    public Mono<Statistics> create() {
        Statistics statistics = new Statistics();
        return postsRepository.findByEnabledTrue().collectList().flatMap(postsList -> {
            // 统计结果为昨天的数据
            LocalDate yesterday = LocalDate.now().minusDays(1L);
            statistics.setDate(yesterday);
            postsList.forEach(posts -> {
                statistics.setViewed(statistics.getViewed() + posts.getViewed());
                statistics.setLikes(statistics.getLikes() + posts.getLikes());
                statistics.setComments(statistics.getComments() + posts.getComment());
            });
            // 统计前天数据，大前天的数据，做差值，计算环比数据
            return statisticsRepository.getByDate(yesterday.minusDays(2L)).flatMap(tda ->
                    statisticsRepository.getByDate(yesterday.minusDays(1L)).map(bys -> {
                        statistics.setOverViewed(this.dayOverDay(statistics.getViewed(), bys.getViewed(), tda.getViewed()));
                        statistics.setOverLikes(this.dayOverDay(statistics.getLikes(), bys.getLikes(), tda.getLikes()));
                        statistics.setOverComments(this.dayOverDay(statistics.getComments(), bys.getComments(), tda.getComments()));
                        return statistics;
                    }));
        }).flatMap(statisticsRepository::insert);
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

    /**
     * over data 计算
     *
     * @param y   昨天数据
     * @param by  前天数据
     * @param tda 大前天数据
     * @return 计算结果
     */
    private double dayOverDay(int y, int by, int tda) {
        if (y - by != 0) {
            if (by - tda == 0) {
                return (y - by) * 100.0;
            }
            // 计算增长率（百分比表示），四舍五入，保留2位小数
            double over = ((y - by) - (by - tda)) * 1.0 / (by - tda) * 100;
            return BigDecimal.valueOf(over).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return 0.0;
    }

}
