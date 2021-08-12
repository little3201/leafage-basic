/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.RoleAuthority;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 角色权限repository
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
@Repository
public interface RoleAuthorityRepository extends ReactiveCrudRepository<RoleAuthority, ObjectId> {

    /**
     * 统计关联角色
     *
     * @param authorityId 权限ID
     * @return 用户数
     */
    Mono<Long> countByAuthorityIdAndEnabledTrue(ObjectId authorityId);

    /**
     * 根据权限查角色
     *
     * @param authorityId 权限主键
     * @return 关联数据集
     */
    Flux<RoleAuthority> findByRoleIdAndEnabledTrue(ObjectId authorityId);

    /**
     * 根据角色查权限
     *
     * @param roleId 角色主键
     * @return 关联数据集
     */
    Flux<RoleAuthority> findByAuthorityIdAndEnabledTrue(ObjectId roleId);
}
