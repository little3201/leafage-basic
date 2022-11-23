/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.vo.PostsVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * posts service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface PostsService extends ReactiveBasicService<PostsDTO, PostsVO, String> {

    /**
     * 分页查询
     *
     * @param page     页码
     * @param size     大小
     * @param sort     排序
     * @param category 分类
     * @return 结果集
     */
    Mono<Page<PostsVO>> retrieve(int page, int size, String sort, String category);

    /**
     * 下一条记录
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<PostsVO> next(String code);

    /**
     * 上一条记录
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<PostsVO> previous(String code);

    /**
     * 自增likes
     *
     * @param code 代码
     * @return 最新 likes
     */
    Mono<Integer> like(String code);

    /**
     * 全文搜索
     *
     * @param keyword 关键字
     * @return 匹配结果
     */
    Flux<PostsVO> search(String keyword);
}
