/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.service.ArticleService;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.basic.assets.vo.DetailsVO;
import top.abeille.basic.assets.vo.StatisticsVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 文章信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/article")
public class ArticleController extends AbstractController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 分页查询信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<ArticleVO> retrieveArticle() {
        return articleService.retrieveAll();
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<DetailsVO> fetchArticle(@PathVariable String code) {
        return articleService.fetchDetailsByCode(code).switchIfEmpty(Mono.empty());
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param articleDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ArticleVO> createArticle(@RequestBody @Valid ArticleDTO articleDTO) {
        return articleService.create(articleDTO);
    }

    /**
     * 根据传入的 articleId 和要修改的数据，修改信息
     *
     * @param code       代码
     * @param articleDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ArticleVO> modifyArticle(@PathVariable String code, @RequestBody @Valid ArticleDTO articleDTO) {
        return articleService.modify(code, articleDTO);
    }

    /**
     * 获取一个月内每日更新数量
     *
     * @return 一个月内每日更新数量
     */
    @GetMapping("/statistics")
    public Flux<StatisticsVO> statistics() {
        return articleService.statistics();
    }

    /**
     * 获取周访问量前十的数据
     *
     * @return 本周访问量前十的数据
     */
    public Flux<Integer> fetchTop10() {
        return Flux.empty();
    }
}
