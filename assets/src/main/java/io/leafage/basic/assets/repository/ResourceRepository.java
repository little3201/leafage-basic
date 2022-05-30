/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Resource;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * resource repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface ResourceRepository extends ReactiveMongoRepository<Resource, ObjectId> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效作品集
     */
    Flux<Resource> findByEnabledTrue(Pageable pageable);

    /**
     * 查询
     *
     * @return 有效作品集
     */
    Flux<Resource> findByEnabledTrue();

    /**
     * 根据分类分页查询
     *
     * @param categoryId 分类ID
     * @param pageable   分页参数
     * @return 有效帖子
     */
    Flux<Resource> findByCategoryIdAndEnabledTrue(ObjectId categoryId, Pageable pageable);

    /**
     * 根据code、enabled查询
     *
     * @param code 代码
     * @return 作品信息
     */
    Mono<Resource> getByCodeAndEnabledTrue(String code);

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
     * @param categoryId 分类ID
     * @return 帖子数
     */
    Mono<Long> countByCategoryIdAndEnabledTrue(ObjectId categoryId);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
