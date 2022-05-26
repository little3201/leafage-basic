package io.leafage.basic.assets.task;

import io.leafage.basic.assets.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticsTasks {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsTasks.class);

    private final StatisticsService statisticsService;

    public StatisticsTasks(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * 创建每日统计数据
     */
    @Scheduled(cron = "1 0 0 * * ?")
    public void everydayCreate() {
        try {
            statisticsService.create().subscribe(statistics ->
                    logger.info("创建每日统计数据任务执行完成: {}", statistics.getDate()));
        } catch (Exception e) {
            logger.error("创建每日统计数据任务执行异常: ", e);
        }
    }

    /**
     * 计算昨日统计数据
     */
    @Scheduled(cron = "30 0 0 * * ?")
    public void yesterdayCalculate() {
        try {
            statisticsService.modify().subscribe(statistics ->
                    logger.info("计算昨日统计数据任务执行完成: {}", statistics.getDate()));
        } catch (Exception e) {
            logger.error("计算昨日统计数据任务执行异常: ", e);
        }
    }

}
