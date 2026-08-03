/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.leafage.hypervisor.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.leafage.hypervisor.system.domain.GroupPrivilege;

import java.util.Collection;
import java.util.List;

/**
 * group privileges repository.
 *
 * @author wq li
 */
@Repository
public interface GroupPrivilegeRepository extends JpaRepository<GroupPrivilege, Long> {

    /**
     * 查询 privilege.
     *
     * @param groupId the pk of privilege.
     * @return the result.
     */
    @Query("""
            SELECT gp 
            FROM GroupPrivilege gp 
            LEFT JOIN FETCH gp.privilege p 
            LEFT JOIN FETCH gp.group g 
            WHERE gp.group.id = :groupId
            """)
    List<GroupPrivilege> findAllByGroupId(Long groupId);

    /**
     * 根据group查privilege
     *
     * @param groupId      the pk of privilege.
     * @param privilegeIds the pk of privilege.
     * @return the result.
     */
    List<GroupPrivilege> findAllByGroupIdAndPrivilegeIdIn(Long groupId, Collection<Long> privilegeIds);

    /**
     * 删除
     *
     * @param roleId the pk of role.
     */
    void deleteByGroupId(Long roleId);

    /**
     * 删除
     *
     * @param roleId       the pk of group.
     * @param privilegeIds the pk of privilege.
     */
    void deleteByGroupIdAndPrivilegeIdNotIn(Long roleId, Collection<Long> privilegeIds);
}
