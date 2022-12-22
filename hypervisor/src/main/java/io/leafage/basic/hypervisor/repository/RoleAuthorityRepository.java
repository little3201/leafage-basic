/*
 *  Copyright 2018-2022 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.RoleAuthority;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * role authority repository
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
