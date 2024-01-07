/*
 *  Copyright 2018-2024 the original author or authors.
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

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * role privileges service
 *
 * @author liwenqiang 2018-07-28 0:29
 **/
public interface RolePrivilegesService {

    /**
     * 关联权限查询
     *
     * @param roleId role主键
     * @return 数据集
     */
    Mono<List<RolePrivileges>> privileges(Long roleId);

    /**
     * 关联role查询
     *
     * @param privilegeId privilege主键
     * @return 数据集
     */
    Mono<List<RolePrivileges>> roles(Long privilegeId);

    /**
     * 角色-组件关系
     *
     * @param roleId       role主键
     * @param privilegeIds 权限信息
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(Long roleId, Set<Long> privilegeIds);
}
