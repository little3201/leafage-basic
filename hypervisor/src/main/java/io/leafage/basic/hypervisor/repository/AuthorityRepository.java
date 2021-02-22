/*
 * Copyright (c) 2019. Abeille All Right Reserved.
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
 * 权限资源dao
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {

    /**
     * 分页查询权限
     *
     * @param pageable 分页参数
     * @return 有效权限
     */
    Flux<Authority> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 资源信息
     */
    Mono<Authority> getByCodeAndEnabledTrue(String code);

    /**
     * 根据权限Id集合查询多条enabled信息
     *
     * @param ids id集合
     * @return 资源信息
     */
    Flux<Authority> findByIdInAndEnabledTrue(Collection<ObjectId> ids);

    /**
     * 根据权限
     *
     * @param codes 代码集合
     * @return 角色信息
     */
    Flux<Authority> findByCodeInAndEnabledTrue(Collection<String> codes);
}
