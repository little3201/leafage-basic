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
     * 执行统计任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void execute() {
        try {
            statisticsService.create().subscribe(statistics ->
                    logger.info("定时记录浏览量，任务执行完成: {}", statistics.getDate()));
        } catch (Exception e) {
            logger.error("定时记录浏览量，任务执行异常: ", e);
        }
    }

}
