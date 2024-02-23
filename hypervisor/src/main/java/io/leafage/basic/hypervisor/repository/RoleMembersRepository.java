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

import io.leafage.basic.hypervisor.domain.RoleMembers;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * role members repository
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface RoleMembersRepository extends R2dbcRepository<RoleMembers, Long> {

    /**
     * 根据user查询关联role
     *
     * @param username user
     * @return 关联的role
     */
    Flux<RoleMembers> findByUsername(String username);

    /**
     * 统计关联role
     *
     * @param roleId roleID
     * @return count
     */
    Mono<Long> countByRoleId(Long roleId);

    /**
     * 根据role查member
     *
     * @param roleId role主键
     * @return 关联数据集
     */
    Flux<RoleMembers> findByRoleId(Long roleId);
}
