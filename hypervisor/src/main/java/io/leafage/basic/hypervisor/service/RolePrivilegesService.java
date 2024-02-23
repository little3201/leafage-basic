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

import io.leafage.basic.hypervisor.domain.RolePrivileges;

import java.util.List;
import java.util.Set;

/**
 * role privileges service.
 *
 * @author wq li 2021/9/27 14:18
 **/
public interface RolePrivilegesService {

    /**
     * 查询关联privilege
     *
     * @param roleId role主键
     * @return 数据集
     */
    List<RolePrivileges> privileges(Long roleId);

    /**
     * 查询关联role
     *
     * @param privilegeId privilege 主键
     * @return 数据集
     */
    List<RolePrivileges> roles(Long privilegeId);

    /**
     * 保存role-privilege关系
     *
     * @param roleId       role主键
     * @param privilegeIds privilegeId集合
     * @return 结果集
     */
    List<RolePrivileges> relation(Long roleId, Set<Long> privilegeIds);
}
