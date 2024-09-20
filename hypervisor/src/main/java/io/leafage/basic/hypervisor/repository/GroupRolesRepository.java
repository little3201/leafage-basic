/*
 *  Copyright 2018-2024 little3201.
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

import io.leafage.basic.hypervisor.domain.GroupRoles;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * group roles repository
 *
 * @author wq li
 */
@Repository
public interface GroupRolesRepository extends R2dbcRepository<GroupRoles, Long> {

    /**
     * 统计关联role
     *
     * @param groupId 组ID
     * @return result
     */
    Mono<Long> countByGroupId(Long groupId);

    /**
     * 根据group查role
     *
     * @param groupId group主键
     * @return 关联数据集
     */
    Flux<GroupRoles> findByGroupId(Long groupId);

    /**
     * 根据role查group
     *
     * @param roleId user
     * @return 关联数据集
     */
    Flux<GroupRoles> findByRoleId(Long roleId);
}
