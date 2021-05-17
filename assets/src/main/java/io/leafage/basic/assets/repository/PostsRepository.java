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
 * 帖子信息repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface PostsRepository extends ReactiveMongoRepository<Posts, ObjectId> {

    /**
     * 分页查询帖子
     *
     * @param pageable 分页参数
     * @return 有效帖子
     */
    Flux<Posts> findByEnabledTrue(Pageable pageable);

    /**
     * 根据分类分页查询帖子
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
     * 统计关联帖子
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
     * 全文搜索
     *
     * @param criteria 搜索内容
     * @return 匹配结果
     */
    Flux<Posts> findAllBy(TextCriteria criteria);
}
