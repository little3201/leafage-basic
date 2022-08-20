/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Group;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * group repository
 *
 * @author liwenqiang 2018/12/20 9:52
 **/
@Repository
public interface GroupRepository extends ReactiveMongoRepository<Group, ObjectId> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效组
     */
    Flux<Group> findByEnabledTrue(Pageable pageable);

    /**
     * 查询所有
     *
     * @return 有效组
     */
    Flux<Group> findByEnabledTrue();

    /**
     * 查询组
     *
     * @param codes 代码集合
     * @return 角色信息
     */
    Flux<Group> findByCodeInAndEnabledTrue(Collection<String> codes);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 组织信息
     */
    Mono<Group> getByCodeAndEnabledTrue(String code);

    /**
     * 是否已存在
     *
     * @param name 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByName(String name);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
