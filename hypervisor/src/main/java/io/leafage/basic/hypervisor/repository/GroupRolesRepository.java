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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * group roles repository.
 *
 * @author wq li 2018/12/17 19:37
 **/
@Repository
public interface GroupRolesRepository extends JpaRepository<GroupRoles, Long> {

    /**
     * 根据group查user
     *
     * @param groupId group主键
     * @return 关联数据集
     */
    List<GroupRoles> findByGroupId(Long groupId);

    /**
     * 根据user查group
     *
     * @param roleId role主键
     * @return 关联数据集
     */
    List<GroupRoles> findByRoleId(Long roleId);

    /**
     * 统计关联user
     *
     * @param groupId groupID
     * @return 用户数
     */
    long countByGroupIdAndEnabledTrue(Long groupId);
}
