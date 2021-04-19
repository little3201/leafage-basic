/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PortfolioDTO;
import io.leafage.basic.assets.service.PortfolioService;
import io.leafage.basic.assets.vo.PortfolioVO;
import org.springframework.http.HttpStatus;
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

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * 分页查询
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<PortfolioVO> retrieve(@RequestParam int page, @RequestParam int size,
                                      String category, String order) {
        if (StringUtils.hasText(category)) {
            return portfolioService.retrieve(page, size, category, order);
        }
        return portfolioService.retrieve(page, size);
    }

    /**
     * 查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<PortfolioVO> fetch(@PathVariable String code) {
        return portfolioService.fetch(code);
    }

    /**
     * 统计记录数
     *
     * @return 记录数
     */
    @GetMapping("/count")
    public Mono<Long> count() {
        return portfolioService.count();
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param portfolioDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PortfolioVO> create(@RequestBody @Valid PortfolioDTO portfolioDTO) {
        return portfolioService.create(portfolioDTO);
    }

    /**
     * 根据传入的代码和要修改的数据，修改信息
     *
     * @param code         代码
     * @param portfolioDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<PortfolioVO> modify(@PathVariable String code, @RequestBody @Valid PortfolioDTO portfolioDTO) {
        return portfolioService.modify(code, portfolioDTO);
    }

}
