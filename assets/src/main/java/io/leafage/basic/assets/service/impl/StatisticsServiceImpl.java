package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
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
    public Mono<StatisticsVO> viewed() {
        return statisticsRepository.getByDate(LocalDate.now().minusDays(1)).map(this::convertOuter);
    }

    @Override
    public Mono<StatisticsVO> viewedSave() {
        Statistics statistics = new Statistics(LocalDate.now().minusDays(1), 0, 0, 0, 0);
        return postsRepository.findByEnabledTrue().collectList().flatMap(postsList -> {
            postsList.forEach(p -> {
                statistics.setViewed(statistics.getViewed() + p.getViewed());
                statistics.setLikes(statistics.getLikes() + p.getLikes());
                statistics.setComment(statistics.getComment() + p.getComment());
            });
            return this.viewed().map(over -> {
                // 设置环比数据
                statistics.setOverViewed((statistics.getViewed() - over.getViewed()) / over.getViewed() * 100);
                return statistics;
            });
        }).flatMap(statisticsRepository::insert).map(this::convertOuter);
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
