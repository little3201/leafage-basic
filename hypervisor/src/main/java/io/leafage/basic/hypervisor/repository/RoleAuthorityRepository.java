/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色权限repository
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
@Repository
public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {

    /**
     * 根据权限查角色
     *
     * @param authorityId 权限主键
     * @return 关联数据集
     */
    List<RoleAuthority> findByRoleId(@NotNull Long authorityId);

    /**
     * 根据角色查权限
     *
     * @param roleId 角色主键
     * @return 关联数据集
     */
    List<RoleAuthority> findByAuthorityId(@NotNull Long roleId);
}
