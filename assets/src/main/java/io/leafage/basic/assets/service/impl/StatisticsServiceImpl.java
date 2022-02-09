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
 * 统计信息 service 接口实现
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
            LocalDate now = LocalDate.now();
            statistics.setDate(now);
            postsList.forEach(posts -> {
                statistics.setViewed(statistics.getViewed() + posts.getViewed());
                statistics.setLikes(statistics.getLikes() + posts.getLikes());
                statistics.setComment(statistics.getComment() + posts.getComment());
            });
            // 统计昨天数据，然后和前天的数据做差值，计算环比数据
            return statisticsRepository.getByDate(now.minusDays(3)).flatMap(bys ->
                    statisticsRepository.getByDate(now.minusDays(2)).map(ys -> {
                        statistics.setOverViewed(this.dayOverDay(statistics.getViewed(), ys.getViewed(), bys.getViewed()));
                        statistics.setOverLikes(this.dayOverDay(statistics.getLikes(), ys.getLikes(), bys.getLikes()));
                        statistics.setOverComment(this.dayOverDay(statistics.getComment(), ys.getComment(), bys.getComment()));
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
     * @param s  最新数据
     * @param y  前一天数据
     * @param by 大前天数据
     * @return 计算结果
     */
    private double dayOverDay(int s, int y, int by) {
        if (s - y != 0) {
            if (y - by == 0) {
                return (s - y) * 100.0;
            }
            // 计算增长率
            double overViewed = ((s - y) - (y - by)) * 1.0 / (y - by) * 100;
            overViewed = BigDecimal.valueOf(overViewed).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return overViewed;
        }
        return 0.0;
    }

}
