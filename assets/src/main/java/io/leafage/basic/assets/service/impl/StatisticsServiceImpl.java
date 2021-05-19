package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
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
    public Mono<Statistics> viewed() {
        return statisticsRepository.getByTimestamp(LocalDate.now().minusDays(1));
    }

    @Override
    public Mono<Statistics> viewedSave() {
        Statistics statistics = new Statistics(LocalDate.now().minusDays(1), 0, 0, 0);
        return postsRepository.findByEnabledTrue().collectList().map(postsList -> {
            postsList.forEach(p -> {
                statistics.setViewed(statistics.getViewed() + p.getViewed());
                statistics.setLikes(statistics.getLikes() + p.getLikes());
                statistics.setComment(statistics.getComment() + p.getComment());
            });
            return statistics;
        }).flatMap(statisticsRepository::insert);
    }
}
