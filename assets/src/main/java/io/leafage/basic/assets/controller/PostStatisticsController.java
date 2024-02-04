package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.PostStatisticsService;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * resource controller.
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/statistics")
public class PostStatisticsController {

    private final Logger logger = LoggerFactory.getLogger(PostStatisticsController.class);

    private final PostStatisticsService postStatisticsService;

    public PostStatisticsController(PostStatisticsService postStatisticsService) {
        this.postStatisticsService = postStatisticsService;
    }

    /**
     * 分页查询浏览量统计
     *
     * @param page 页码
     * @param size 大小
     * @return 查询到数据，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Page<PostStatisticsVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<PostStatisticsVO> voFlux;
        try {
            voFlux = postStatisticsService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Statistics viewed occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

}
