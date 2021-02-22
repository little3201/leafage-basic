/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * 账户信息repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, ObjectId> {

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 账户信息
     */
    Mono<Account> getByCodeAndEnabledTrue(String code);
}
