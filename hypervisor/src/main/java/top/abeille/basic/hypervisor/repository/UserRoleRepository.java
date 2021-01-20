/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.hypervisor.entity.UserRole;

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
