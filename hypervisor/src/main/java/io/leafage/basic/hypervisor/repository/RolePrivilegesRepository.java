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

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * role privilege repository
 *
 * @author wq li
 */
@Repository
public interface RolePrivilegesRepository extends R2dbcRepository<RolePrivileges, Long> {

    /**
     * 统计关联role
     *
     * @param roleId role主键
     * @return count
     */
    Mono<Long> countByRoleId(Long roleId);

    /**
     * 根据role查询
     *
     * @param roleId role主键
     * @return 关联数据集
     */
    Flux<RolePrivileges> findByRoleId(Long roleId);

    /**
     * 根据privilege查询
     *
     * @param privilegeId privilege主键
     * @return 关联数据集
     */
    Flux<RolePrivileges> findByPrivilegeId(Long privilegeId);
}
