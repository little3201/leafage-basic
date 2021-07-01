/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.UserGroup;
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
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    /**
     * 根据分组查用户
     *
     * @param groupId 分组主键
     * @return 关联数据集
     */
    List<UserGroup> findByGroupId(@NotNull Long groupId);

    /**
     * 根据用户查分组
     *
     * @param userId 用户主键
     * @return 关联数据集
     */
    List<UserGroup> findByUserId(@NotNull Long userId);
}
