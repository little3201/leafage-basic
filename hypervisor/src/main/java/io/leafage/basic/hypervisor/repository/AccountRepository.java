/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Account;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * account repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, ObjectId> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效记录
     */
    Flux<Account> findByEnabledTrue(Pageable pageable);

    /**
     * 根据账号查询
     *
     * @param username 账号
     * @return 账户信息
     */
    Mono<Account> getByUsernameAndEnabledTrue(String username);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
