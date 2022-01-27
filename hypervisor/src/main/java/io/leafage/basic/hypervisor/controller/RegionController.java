package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * region controller
 *
 * @author liwenqiang 2021/08/20 17:08
 **/
@RestController
@RequestMapping("/region")
public class RegionController {

    private final Logger logger = LoggerFactory.getLogger(RegionController.class);

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Flux<RegionVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Flux<RegionVO> voFlux;
        try {
            voFlux = regionService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve region occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据 code 查询
     *
     * @param code 业务id
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<RegionVO>> fetch(@PathVariable Long code) {
        Mono<RegionVO> voMono;
        try {
            voMono = regionService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch region occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 统计记录数
     *
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = regionService.count();
        } catch (Exception e) {
            logger.error("Count region occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 查询下级数据
     *
     * @return 查询到的数据，否则返回空
     */
    @GetMapping("/{code}/lower")
    public ResponseEntity<Flux<RegionVO>> lower(@PathVariable Long code) {
        Flux<RegionVO> voFlux;
        try {
            voFlux = regionService.lower(code);
        } catch (Exception e) {
            logger.info("Retrieve region lower occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

}
