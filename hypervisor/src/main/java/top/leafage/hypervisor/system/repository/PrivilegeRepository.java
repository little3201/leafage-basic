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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.leafage.hypervisor.system.domain.Privilege;

import java.util.Collection;
import java.util.List;

/**
 * privilege repository.
 *
 * @author wq li
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>, JpaSpecificationExecutor<Privilege> {

    /**
     * is exists.
     *
     * @param name name.
     * @return if exists return true or false.
     */
    boolean existsByName(String name);

    /**
     * Finds all records by superior ID.
     *
     * @param superiorId The superior ID.
     * @return A list of privileges.
     */
    List<Privilege> findAllBySuperiorId(Long superiorId);

    /**
     * Group 直接配置的 Privilege
     *
     * @return result.
     */
    @Query("""
            SELECT DISTINCT p.id 
            FROM Group g
            JOIN g.members u
            JOIN g.groupPrivileges gp
            JOIN gp.privilege p
            WHERE u.username = ?#{ principal?.name }
            """)
    List<Long> findGroupPrivilegeIds();

    /**
     * 通过 Group → Role 继承的 Privilege
     *
     * @return result.
     */
    @Query("""
            SELECT DISTINCT p.id 
            FROM Group g
            JOIN g.members u
            JOIN g.roles r
            JOIN r.rolePrivileges rp
            JOIN rp.privilege p
            WHERE u.username = ?#{ principal?.name }
            """)
    List<Long> findGroupRolePrivilegeIds();

    /**
     * Role 直接配置的 Privilege
     *
     * @return result.
     */
    @Query("""
            SELECT DISTINCT p.id 
            FROM User u
            JOIN u.roles r
            JOIN r.rolePrivileges rp
            JOIN rp.privilege p
            WHERE u.username = ?#{ principal?.name }
            """)
    List<Long> findRolePrivilegeIds();

    /**
     * Counts the number of records by superior ID.
     *
     * @param superiorIds The pk of superiors.
     * @return The count of records.
     */
    @Query("SELECT t.superiorId, COUNT(t.id) FROM Privilege t WHERE t.superiorId IN :superiorIds GROUP BY t.superiorId")
    List<Object[]> countBySuperiorIdsGrouped(Collection<Long> superiorIds);

    /**
     * enable a record by pk.
     *
     * @param id the pk.
     * @return result.
     */
    @Modifying
    @Query("UPDATE Privilege t SET t.enabled = true WHERE t.id = :id AND t.enabled = false")
    int enableById(Long id);

    /**
     * disable a record by pk.
     *
     * @param id the pk.
     * @return result.
     */
    @Modifying
    @Query("UPDATE Privilege t SET t.enabled = false WHERE t.id = :id AND t.enabled = true")
    int disableById(Long id);
}
