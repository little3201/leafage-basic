package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.bo.StatisticsBO;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
     * 查询周期统计
     *
     * @param page 页码
     * @param size 大小
     * @return 查询到数据，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Mono<Page<StatisticsVO>>> retrieve(@RequestParam int page, @RequestParam int size) {
        Mono<Page<StatisticsVO>> pageMono;
        try {
            pageMono = statisticsService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Statistics periodicity occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pageMono);
    }

    /**
     * 查询周期统计
     *
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/total")
    public ResponseEntity<Mono<StatisticsBO>> fetch() {
        Mono<StatisticsBO> voMono;
        try {
            voMono = statisticsService.fetch();
        } catch (Exception e) {
            logger.error("Statistics total occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

}
