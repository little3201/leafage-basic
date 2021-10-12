package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * statistics controller
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
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询到数据，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Flux<StatisticsVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Flux<StatisticsVO> voFlux;
        try {
            voFlux = statisticsService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Statistics viewed occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 环比查询
     *
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/over")
    public ResponseEntity<Mono<StatisticsVO>> over() {
        Mono<StatisticsVO> statisticsMono;
        try {
            statisticsMono = statisticsService.over();
        } catch (Exception e) {
            logger.error("Statistics viewed occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(statisticsMono);
    }
}
