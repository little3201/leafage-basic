/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Authority;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collection;

/**
 * authority repository
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, ObjectId> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效权限
     */
    Flux<Authority> findByEnabledTrue(Pageable pageable);

    /**
     * 查询所有
     *
     * @return 有效权限
     */
    Flux<Authority> findByEnabledTrue();

    /**
     * 根据类型查询
     *
     * @param type 类型
     * @return 有效菜单
     */
    Flux<Authority> findByTypeAndEnabledTrue(Character type);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 权限信息
     */
    Mono<Authority> getByCodeAndEnabledTrue(String code);

    /**
     * 根据code集合查询
     *
     * @param codes 代码集合
     * @return 权限信息
     */
    Flux<Authority> findByCodeInAndEnabledTrue(Collection<String> codes);

    /**
     * 是否已存在
     *
     * @param name 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByName(String name);
}
