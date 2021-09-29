package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.entity.Posts;
import io.leafage.basic.assets.entity.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * statistics service 实现
 *
 * @author liwenqiang 2021/09/29 15:30
 **/
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final PostsRepository postsRepository;

    public StatisticsServiceImpl(StatisticsRepository statisticsRepository, PostsRepository postsRepository) {
        this.statisticsRepository = statisticsRepository;
        this.postsRepository = postsRepository;
    }

    @Override
    public Page<StatisticsVO> retrieve(int page, int size) {
        return statisticsRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public StatisticsVO fetch() {
        Statistics statistics = statisticsRepository.getByDate(LocalDate.now().minusDays(1L));
        if (statistics == null) {
            return null;
        }
        return this.convertOuter(statistics);
    }

    @Override
    public Statistics create() {
        Statistics statistics = new Statistics(LocalDate.now().minusDays(1), 0, 0.0,
                0, 0.0, 0, 0.0);
        List<Posts> posts = postsRepository.findByEnableTrue();
        posts.forEach(p -> {
            statistics.setViewed(statistics.getViewed() + p.getViewed());
            statistics.setLikes(statistics.getLikes() + p.getLikes());
            statistics.setComment(statistics.getComment() + p.getComment());
        });
        return statisticsRepository.saveAndFlush(statistics);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param statistics 信息
     * @return 输出转换后的vo对象
     */
    private StatisticsVO convertOuter(Statistics statistics) {
        StatisticsVO vo = new StatisticsVO();
        BeanUtils.copyProperties(statistics, vo);
        return vo;
    }
}
