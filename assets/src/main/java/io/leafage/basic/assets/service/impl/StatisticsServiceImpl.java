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
    public Mono<StatisticsVO> fetch() {
        return statisticsRepository.getByDate(LocalDate.now().minusDays(1)).map(this::convertOuter);
    }

    @Override
    public Mono<Statistics> create() {
        Statistics statistics = new Statistics(LocalDate.now().minusDays(1), 0, 0.0, 0, 0);
        return postsRepository.findByEnabledTrue().collectList().flatMap(postsList -> {
            postsList.forEach(p -> {
                statistics.setViewed(statistics.getViewed() + p.getViewed());
                statistics.setLikes(statistics.getLikes() + p.getLikes());
                statistics.setComment(statistics.getComment() + p.getComment());
            });
            // 统计昨天数据，然后和前天的数据做差值，计算环比数据
            return this.statisticsRepository.getByDate(LocalDate.now().minusDays(2)).map(over -> {
                // 设置环比数据
                if (over.getViewed() == 0) {
                    return statistics;
                }
                //  两位小数，四舍五入
                double overViewed = (statistics.getViewed() - over.getViewed()) * 1.0 / over.getViewed() * 100;
                overViewed = BigDecimal.valueOf(overViewed).setScale(2, RoundingMode.HALF_UP).doubleValue();
                statistics.setOverViewed(overViewed);
                return statistics;
            }).switchIfEmpty(Mono.just(statistics));
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
}
