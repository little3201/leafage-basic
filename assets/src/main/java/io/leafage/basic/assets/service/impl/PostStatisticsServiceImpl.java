package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.PostStatistics;
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import io.leafage.basic.assets.service.PostStatisticsService;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * statistics service impl.
 *
 * @author liwenqiang 2021/09/29 15:30
 **/
@Service
public class PostStatisticsServiceImpl implements PostStatisticsService {

    private final PostStatisticsRepository postStatisticsRepository;

    public PostStatisticsServiceImpl(PostStatisticsRepository postStatisticsRepository) {
        this.postStatisticsRepository = postStatisticsRepository;
    }

    @Override
    public Page<PostStatisticsVO> retrieve(int page, int size) {
        return postStatisticsRepository.findAll(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public PostStatistics create() {
        PostStatistics postStatistics = new PostStatistics();
        LocalDate now = LocalDate.now();
        postStatistics.setDate(now);
        // 计算较前日增长率。计算日期数据还没生成，所以前一天减去2，大前天减去3
        PostStatistics ys = postStatisticsRepository.getByDate(now.minusDays(2));
        PostStatistics bys = postStatisticsRepository.getByDate(now.minusDays(3));
        postStatistics.setOverViewed(this.dayOverDay(postStatistics.getViewed(), ys.getViewed(), bys.getViewed()));
        postStatistics.setOverLikes(this.dayOverDay(postStatistics.getLikes(), ys.getLikes(), bys.getLikes()));
        postStatistics.setOverComments(this.dayOverDay(postStatistics.getComments(), ys.getComments(), bys.getComments()));
        return postStatisticsRepository.saveAndFlush(postStatistics);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param postStatistics 信息
     * @return 输出转换后的vo对象
     */
    private PostStatisticsVO convertOuter(PostStatistics postStatistics) {
        PostStatisticsVO vo = new PostStatisticsVO();
        BeanUtils.copyProperties(postStatistics, vo);
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
