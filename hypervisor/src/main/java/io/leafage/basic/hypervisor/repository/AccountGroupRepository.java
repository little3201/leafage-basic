/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.AccountGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * account group repository.
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface AccountGroupRepository extends JpaRepository<AccountGroup, Long> {

    /**
     * 根据分组查用户
     *
     * @param groupId 分组主键
     * @return 关联数据集
     */
    List<AccountGroup> findByGroupId(Long groupId);

    /**
     * 根据用户查分组
     *
     * @param userId 用户主键
     * @return 关联数据集
     */
    List<AccountGroup> findByUserId(Long userId);
}
