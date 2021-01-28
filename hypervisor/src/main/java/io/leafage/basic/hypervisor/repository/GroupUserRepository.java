/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.GroupUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface GroupUserRepository extends ReactiveCrudRepository<GroupUser, String> {

    /**
     * 统计关联用户
     *
     * @param groupId 组ID
     * @return 用户数
     */
    Mono<Long> countByGroupIdAndEnabledTrue(@NotNull String groupId);
}
