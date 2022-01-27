/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.AccountRole;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 用户角色repository
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface AccountRoleRepository extends ReactiveCrudRepository<AccountRole, ObjectId> {

    /**
     * 根据账号查询关联角色
     *
     * @param accountId 账号主键
     * @return 关联的角色
     */
    Flux<AccountRole> findByAccountIdAndEnabledTrue(ObjectId accountId);

    /**
     * 统计关联用户
     *
     * @param roleId 角色ID
     * @return 用户数
     */
    Mono<Long> countByRoleIdAndEnabledTrue(ObjectId roleId);

    /**
     * 根据角色查用户
     *
     * @param roleId 角色主键
     * @return 关联数据集
     */
    Flux<AccountRole> findByRoleIdAndEnabledTrue(ObjectId roleId);
}