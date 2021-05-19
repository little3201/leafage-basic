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
     * 统计昨日浏览量
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void viewed() {
        try {
            statisticsService.viewedSave().subscribe(statistics ->
                    logger.info("定时记录浏览量，任务执行完成: {}", statistics.getTimestamp()));
        } catch (Exception e) {
            logger.error("定时记录浏览量，任务执行异常: ", e);
        }
    }

}
