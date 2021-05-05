/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.service.PortfolioService;
import io.leafage.basic.assets.vo.PortfolioVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 作品集信息controller
 *
 * @author liwenqiang 2020/2/20 9:54
 **/
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * 分页查询
     *
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Flux<PortfolioVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                      String category, String order) {
        Flux<PortfolioVO> voFlux;
        try {
            if (StringUtils.hasText(category)) {
                voFlux = portfolioService.retrieve(page, size, category, order);
            } else {
                voFlux = portfolioService.retrieve(page, size);
            }
        } catch (Exception e) {
            logger.error("Retrieve portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/{code}")
    public ResponseEntity<Mono<PortfolioVO>> fetch(@PathVariable String code) {
        Mono<PortfolioVO> voMono;
        try {
            voMono = portfolioService.fetch(code);
        } catch (Exception e) {
            logger.error("Fetch portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 统计记录数
     *
     * @return 查询到数据，异常时返回204
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = portfolioService.count();
        } catch (Exception e) {
            logger.error("Count portfolio occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param portfolioDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<PortfolioVO>> create(@RequestBody @Valid PortfolioDTO portfolioDTO) {
        Mono<PortfolioVO> voMono;
        try {
            voMono = portfolioService.create(portfolioDTO);
        } catch (Exception e) {
            logger.error("Create portfolio occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code         代码
     * @param portfolioDTO 要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{code}")
    public ResponseEntity<Mono<PortfolioVO>> modify(@PathVariable String code, @RequestBody @Valid PortfolioDTO portfolioDTO) {
        Mono<PortfolioVO> voMono;
        try {
            voMono = portfolioService.modify(code, portfolioDTO);
        } catch (Exception e) {
            logger.error("Modify portfolio occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

}
