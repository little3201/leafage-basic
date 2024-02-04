/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.GroupMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * group members repository.
 *
 * @author wq li 2018/12/17 19:37
 **/
@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembers, Long> {

    /**
     * 根据group查user
     *
     * @param groupId group主键
     * @return 关联数据集
     */
    List<GroupMembers> findByGroupId(Long groupId);

    /**
     * 根据user查group
     *
     * @param username 用户名
     * @return 关联数据集
     */
    List<GroupMembers> findByUsername(String username);

    /**
     * 统计关联user
     *
     * @param groupId groupID
     * @return 用户数
     */
    long countByGroupIdAndEnabledTrue(Long groupId);
}
