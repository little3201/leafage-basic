/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Posts;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * posts repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface PostsRepository extends ReactiveMongoRepository<Posts, ObjectId> {

    /**
     * 查询
     *
     * @return 有效帖子
     */
    Flux<Posts> findByEnabledTrue();

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效帖子
     */
    Flux<Posts> findByEnabledTrue(Pageable pageable);

    /**
     * 根据分类分页查询
     *
     * @param categoryId 分类ID
     * @param pageable   分页参数
     * @return 有效帖子
     */
    Flux<Posts> findByCategoryIdAndEnabledTrue(ObjectId categoryId, Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 帖子信息
     */
    Mono<Posts> getByCodeAndEnabledTrue(String code);

    /**
     * 关联统计
     *
     * @param categoryId 分类ID
     * @return 帖子数
     */
    Mono<Long> countByCategoryIdAndEnabledTrue(ObjectId categoryId);

    /**
     * 查询下一相邻的记录
     *
     * @param id       主键
     * @param pageable 分页对象
     * @return 帖子信息
     */
    Flux<Posts> findByIdGreaterThanAndEnabledTrue(ObjectId id, Pageable pageable);

    /**
     * 查询上一相邻的记录
     *
     * @param id       主键
     * @param pageable 分页对象
     * @return 帖子信息
     */
    Flux<Posts> findByIdLessThanAndEnabledTrue(ObjectId id, Pageable pageable);

    /**
     * 根据title查询
     *
     * @param title 标题
     * @return 匹配结果
     */
    Flux<Posts> findByTitle(String title, TextCriteria criteria);

    /**
     * 是否已存在
     *
     * @param title 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByTitle(String title);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
