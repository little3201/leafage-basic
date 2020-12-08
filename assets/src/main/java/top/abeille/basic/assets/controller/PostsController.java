/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.PostsDTO;
import top.abeille.basic.assets.service.PostsService;
import top.abeille.basic.assets.vo.DetailsVO;
import top.abeille.basic.assets.vo.PostsVO;

import javax.validation.Valid;

/**
 * 文章信息controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Validated
@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    /**
     * 分页查询信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<PostsVO> retrieveArticle(@RequestParam int page, @RequestParam @Range(max = 50) int size) {
        return postsService.retrieveAll(page, size);
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<DetailsVO> fetchArticle(@PathVariable String code) {
        return postsService.fetchDetailsByCode(code);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param postsDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PostsVO> createArticle(@RequestBody @Valid PostsDTO postsDTO) {
        return postsService.create(postsDTO);
    }

    /**
     * 根据传入的 articleId 和要修改的数据，修改信息
     *
     * @param code     代码
     * @param postsDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<PostsVO> modifyArticle(@PathVariable String code, @RequestBody @Valid PostsDTO postsDTO) {
        return postsService.modify(code, postsDTO);
    }

}
