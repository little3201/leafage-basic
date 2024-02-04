/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.RolePrivileges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * role privileges repository.
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
@Repository
public interface RolePrivilegesRepository extends JpaRepository<RolePrivileges, Long> {

    /**
     * 根据role查privilege
     *
     * @param roleId privilege主键
     * @return 关联数据集
     */
    List<RolePrivileges> findByRoleId(Long roleId);

    /**
     * 根据privilege查role
     *
     * @param privilegeId privilege主键
     * @return 关联数据集
     */
    List<RolePrivileges> findByPrivilegeId(Long privilegeId);

    /**
     * 统计关联privilege
     *
     * @param roleId role主键
     * @return 用户数
     */
    long countByRoleIdAndEnabledTrue(Long roleId);
}
