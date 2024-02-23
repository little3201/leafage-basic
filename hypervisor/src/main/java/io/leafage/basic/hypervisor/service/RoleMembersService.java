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

package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.RoleMembers;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * role members service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface RoleMembersService {

    /**
     * 查询关联user
     *
     * @param roleId role主键
     * @return 数据集
     */
    Mono<List<RoleMembers>> members(Long roleId);

    /**
     * 查询关联role
     *
     * @param username user
     * @return 数据集
     */
    Mono<List<RoleMembers>> roles(String username);

    /**
     * role-user关系
     *
     * @param roleId    role 主键
     * @param usernames username集合
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(Long roleId, Set<String> usernames);
}
