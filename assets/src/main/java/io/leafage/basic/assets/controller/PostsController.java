/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * posts controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
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
    public Flux<PostsVO> retrieve(@RequestParam int page, @RequestParam int size, String order) {
        return postsService.retrieve(page, size, order);
    }

    /**
     * 根据传入的代码查询信息
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}")
    public Mono<PostsContentVO> fetch(@PathVariable String code) {
        return postsService.fetchContent(code);
    }

    /**
     * 根据传入的代码查询下一条记录
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}/next")
    public Mono<PostsVO> fetchNext(@PathVariable String code) {
        return postsService.nextPosts(code);
    }

    /**
     * 根据传入的代码查询上一条记录
     *
     * @param code 代码
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{code}/previous")
    public Mono<PostsVO> fetchPrevious(@PathVariable String code) {
        return postsService.previousPosts(code);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param postsDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PostsVO> create(@RequestBody @Valid PostsDTO postsDTO) {
        return postsService.create(postsDTO);
    }

    /**
     * 根据传入的数据修改信息
     *
     * @param code     代码
     * @param postsDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<PostsVO> modify(@PathVariable String code, @RequestBody @Valid PostsDTO postsDTO) {
        return postsService.modify(code, postsDTO);
    }

}
