/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 帖子信息 service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface PostsService extends ReactiveBasicService<PostsDTO, PostsVO> {

    /**
     * 按照分页和分类进行查询并排序
     *
     * @param page     分页
     * @param size     大小
     * @param order    排序
     * @param category 分类
     * @return 结果集
     */
    Flux<PostsVO> retrieve(int page, int size, String category, String order);

    /**
     * 根据代码查询详细信息
     *
     * @param code 代码
     * @return 详细信息
     */
    Mono<PostsContentVO> details(String code);

    /**
     * 根据代码查询内容
     *
     * @param code 代码
     * @return 详细信息
     */
    Mono<ContentVO> content(String code);

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

    /**
     * 是否已存在
     *
     * @param title 名称
     * @return true-是，false-否
     */
    Mono<Boolean> exists(String title);
}
