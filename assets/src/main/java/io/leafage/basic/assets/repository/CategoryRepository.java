/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 类别信息repository
 *
 * @author liwenqiang 2020/2/13 22:01
 **/
@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

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
     * 根据ID查询alias
     *
     * @param id 主键
     * @return alias
     */
    @Query(value = "{ 'id' : ?0 }", fields = "{ 'alias' : 1}")
    Mono<Category> getAliasById(String id);
}
