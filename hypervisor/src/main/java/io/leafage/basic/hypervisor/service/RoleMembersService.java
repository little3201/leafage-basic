package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.RoleMembers;

import java.util.List;
import java.util.Set;

/**
 * account role service.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
public interface RoleMembersService {

    /**
     * 查询关联privilege
     *
     * @param roleId role主键
     * @return 数据集
     */
    List<RoleMembers> members(Long roleId);

    /**
     * 查询关联role
     *
     * @param username 用户名
     * @return 数据集
     */
    List<RoleMembers> roles(String username);

    /**
     * 保存role-privilege关系
     *
     * @param roleId role主键
     * @param users  user集合
     * @return 结果集
     */
    List<RoleMembers> relation(Long roleId, Set<String> users);
}
