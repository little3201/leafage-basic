/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Category;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 类目信息repository
 *
 * @author liwenqiang 2020/2/13 22:01
 **/
@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category, ObjectId> {

    /**
     * 查询所有类别
     *
     * @return 有效类别
     */
    Flux<Category> findByEnabledTrue();

    /**
     * 分页查询类别
     *
     * @param pageable 分页参数
     * @return 有效类别
     */
    Flux<Category> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 类别信息
     */
    Mono<Category> getByCodeAndEnabledTrue(String code);

    /**
     * 是否已存在
     *
     * @param alias 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByAlias(String alias);
}
