package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 统计信息controller
 *
 * @author liwenqiang 2021/5/19 13:54
 **/
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * 浏览量统计
     *
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/viewed")
    public ResponseEntity<Mono<StatisticsVO>> viewed() {
        Mono<StatisticsVO> statisticsMono;
        try {
            statisticsMono = statisticsService.viewed();
        } catch (Exception e) {
            logger.error("Statistics viewed occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(statisticsMono);
    }
}
