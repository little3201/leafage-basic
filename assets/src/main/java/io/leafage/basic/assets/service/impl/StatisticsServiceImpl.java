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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * statistics service impl.
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
    public Statistics create() {
        Statistics statistics = new Statistics();
        List<Posts> posts = postsRepository.findByEnabledTrue();
        posts.forEach(p -> {
            statistics.setViewed(statistics.getViewed() + p.getViewed());
            statistics.setLikes(statistics.getLikes() + p.getLikes());
            statistics.setComments(statistics.getComments() + p.getComments());
        });
        LocalDate now = LocalDate.now();
        statistics.setDate(now);
        // 计算较前日增长率。计算日期数据还没生成，所以前一天减去2，大前天减去3
        Statistics ys = statisticsRepository.getByDate(now.minusDays(2));
        Statistics bys = statisticsRepository.getByDate(now.minusDays(3));
        statistics.setOverViewed(this.dayOverDay(statistics.getViewed(), ys.getViewed(), bys.getViewed()));
        statistics.setOverLikes(this.dayOverDay(statistics.getLikes(), ys.getLikes(), bys.getLikes()));
        statistics.setOverComment(this.dayOverDay(statistics.getComments(), ys.getComments(), bys.getComments()));
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

    /**
     * over data 计算
     *
     * @param s  最新数据
     * @param y  前一天数据
     * @param by 大前天数据
     * @return 计算结果
     */
    private double dayOverDay(int s, int y, int by) {
        if (s - y != 0 && y - by != 0) {
            double overViewed = ((s - y) - (y - by)) * 1.0 / (y - by) * 100;
            overViewed = BigDecimal.valueOf(overViewed).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return overViewed;
        }
        return 0.0;
    }

}
