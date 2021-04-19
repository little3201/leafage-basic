/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户组repository
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 根据用户ID查询
     *
     * @param userId 用户主键
     * @return 集合
     */
    List<UserRole> findByUserId(@NotNull Long userId);

    /**
     * 根据用户ID删除
     *
     * @param userId 用户主键
     */
    void deleteByUserId(@NotNull Long userId);
}
