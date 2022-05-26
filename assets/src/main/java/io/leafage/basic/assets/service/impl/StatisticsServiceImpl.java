package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.ResourceRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsTotalVO;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private final ResourceRepository resourceRepository;
    private final StatisticsRepository statisticsRepository;

    public StatisticsServiceImpl(PostsRepository postsRepository, ResourceRepository resourceRepository,
                                 StatisticsRepository statisticsRepository) {
        this.postsRepository = postsRepository;
        this.resourceRepository = resourceRepository;
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public Mono<Page<StatisticsVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifyTime"));
        Flux<StatisticsVO> voFlux = statisticsRepository.findByEnabledTrue(pageRequest).map(this::convertOuter);

        Mono<Long> count = statisticsRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<StatisticsTotalVO> fetch() {
        return postsRepository.findByEnabledTrue().collectList().map(postsList -> {
            StatisticsTotalVO totalVO = new StatisticsTotalVO();
            postsList.forEach(posts -> {
                totalVO.setViewed(posts.getViewed() + totalVO.getViewed());
                totalVO.setLikes(posts.getLikes() + totalVO.getLikes());
                totalVO.setComments(posts.getComments() + totalVO.getComments());
            });
            return totalVO;
        }).flatMap(vo -> resourceRepository.findByEnabledTrue().collectList().map(resources -> {
            resources.forEach(resource -> vo.setDownloads(vo.getDownloads() + resource.getDownloads()));
            return vo;
        }));
    }

    @Override
    public Mono<Statistics> create() {
        Statistics statistics = new Statistics();
        statistics.setDate(LocalDate.now());
        // 统计昨天数据，前天的数据，做差值，计算环比数据
        return statisticsRepository.insert(statistics);
    }

    @Override
    public Mono<Statistics> modify() {
        return statisticsRepository.getByDate(LocalDate.now().minusDays(1L)).flatMap(ysd ->
                statisticsRepository.getByDate(LocalDate.now().minusDays(2L)).map(bys -> {
                    ysd.setOverViewed(this.dayOverDay(ysd.getViewed(), bys.getViewed()));
                    ysd.setOverLikes(this.dayOverDay(ysd.getLikes(), bys.getLikes()));
                    ysd.setOverComments(this.dayOverDay(ysd.getComments(), bys.getComments()));
                    return ysd;
                })).flatMap(statisticsRepository::save);
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
     * @param y  昨天数据
     * @param by 前天数据
     * @return 计算结果
     */
    private double dayOverDay(int y, int by) {
        if (y - by != 0) {
            // 计算增长率（百分比表示），四舍五入，保留2位小数
            double over = (y - by) * 1.0 / by * 100;
            return BigDecimal.valueOf(over).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return 0.0;
    }

}
