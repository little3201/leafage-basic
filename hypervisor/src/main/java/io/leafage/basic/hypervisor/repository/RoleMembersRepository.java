/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.RoleMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * role members repository.
 *
 * @author wq li 2018/12/17 19:37
 **/
@Repository
public interface RoleMembersRepository extends JpaRepository<RoleMembers, Long> {

    /**
     * 根据user主键ID查询
     *
     * @param username 用户名
     * @return 集合
     */
    List<RoleMembers> findByUsername(String username);

    /**
     * 根据role查询
     *
     * @param roleId role主键
     * @return 关联数据集
     */
    List<RoleMembers> findByRoleId(Long roleId);

    /**
     * 统计关联user
     *
     * @param roleId role主键
     * @return 用户数
     */
    long countByRoleIdAndEnabledTrue(Long roleId);
}
