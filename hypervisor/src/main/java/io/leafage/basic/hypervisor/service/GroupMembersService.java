package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.GroupMembers;

import java.util.List;
import java.util.Set;

/**
 * group members service.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
public interface GroupMembersService {

    /**
     * 查询关联 user
     *
     * @param groupId group主键
     * @return 数据集
     */
    List<GroupMembers> members(Long groupId);

    /**
     * 查询关联 group
     *
     * @param username 用户名
     * @return 数据集
     */
    List<GroupMembers> groups(String username);

    /**
     * 保存group-members
     *
     * @param groupId group主键
     * @param users   user集合
     * @return 结果集
     */
    List<GroupMembers> relation(Long groupId, Set<String> users);
}
