/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

/**
 * 用户角色repository
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, ObjectId> {

    /**
     * 根据用户id查询关联角色
     *
     * @param userId 用户ID
     * @return 关联的角色
     */
    Flux<UserRole> findByUserIdAndEnabledTrue(@NotNull ObjectId userId);

    /**
     * 统计关联用户
     *
     * @param roleId 角色ID
     * @return 用户数
     */
    Mono<Long> countByRoleIdAndEnabledTrue(@NotNull ObjectId roleId);

    /**
     * 根据角色查用户
     *
     * @param roleId 角色主键
     * @return 关联数据集
     */
    Flux<UserRole> findByRoleId(@NotNull ObjectId roleId);

    /**
     * 根据用户查角色
     *
     * @param userId 用户主键
     * @return 关联数据集
     */
    Flux<UserRole> findByUserId(@NotNull ObjectId userId);
}
