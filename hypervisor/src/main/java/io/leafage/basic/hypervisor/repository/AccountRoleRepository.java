/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * account role repository.
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    /**
     * 根据账号主键ID查询
     *
     * @param accountId 用户主键
     * @return 集合
     */
    List<AccountRole> findByAccountId(Long accountId);

    /**
     * 根据角色ID查询
     *
     * @param roleId 角色主键
     * @return 关联数据集
     */
    List<AccountRole> findByRoleId(Long roleId);

    /**
     * 统计关联账号
     *
     * @param roleId 角色ID
     * @return 用户数
     */
    long countByRoleIdAndEnabledTrue(Long roleId);
}
