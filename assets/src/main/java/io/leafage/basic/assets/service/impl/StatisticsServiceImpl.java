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
    public Statistics create() {
        Statistics statistics = new Statistics();
        statistics.setDate(LocalDate.now());
        List<Posts> posts = postsRepository.findByEnabledTrue();
        posts.forEach(p -> {
            statistics.setViewed(statistics.getViewed() + p.getViewed());
            statistics.setLikes(statistics.getLikes() + p.getLikes());
            statistics.setComment(statistics.getComment() + p.getComment());
        });
        // 计算较前日增长率。计算日期数据还没生成，所以前一天减去2，大前天减去3
        Statistics ys = statisticsRepository.getByDate(LocalDate.now().minusDays(2));
        Statistics bys = statisticsRepository.getByDate(LocalDate.now().minusDays(3));
        statistics.setOverViewed(this.overViewed(statistics.getViewed(), ys.getViewed(), bys.getViewed()));
        statistics.setOverLikes(this.overLikes(statistics.getLikes(), ys.getLikes(), bys.getLikes()));
        statistics.setOverComment(this.overComment(statistics.getComment(), ys.getComment(), bys.getComment()));
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
     * 浏览量
     *
     * @param sv  最新数据
     * @param yv  前一天数据
     * @param byv 大前天数据
     * @return 计算结果
     */
    private double overViewed(int sv, int yv, int byv) {
        if (yv > 0 && yv - byv != 0) {
            double overViewed = (sv - yv) * 1.0 / (yv - byv) * 100;
            overViewed = BigDecimal.valueOf(overViewed).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return overViewed;
        }
        return 0.0;
    }

    /**
     * 喜欢数
     *
     * @param sl  最新数据
     * @param yl  前一天数据
     * @param byl 大前天数据
     * @return 计算结果
     */
    private double overLikes(int sl, int yl, int byl) {
        if (yl > 0 && yl - byl != 0) {
            double overLikes = (sl - yl) * 1.0 / (yl - byl) * 100;
            overLikes = BigDecimal.valueOf(overLikes).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return overLikes;
        }
        return 0.0;
    }

    /**
     * 评论量
     *
     * @param sc  最新数据
     * @param yc  前一天数据
     * @param byc 大前天数据
     * @return 计算结果
     */
    private double overComment(int sc, int yc, int byc) {
        if (yc > 0 && yc - byc != 0) {
            double overComment = (sc - yc) * 1.0 / (yc - byc) * 100;
            overComment = BigDecimal.valueOf(overComment).setScale(2, RoundingMode.HALF_UP).doubleValue();
            return overComment;
        }
        return 0.0;
    }
}
