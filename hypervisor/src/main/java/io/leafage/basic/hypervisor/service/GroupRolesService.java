package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.GroupRoles;

import java.util.List;
import java.util.Set;

/**
 * group roles service.
 *
 * @author wq li 2024/2/2 15:20
 **/
public interface GroupRolesService {

    /**
     * 查询关联 role
     *
     * @param groupId group主键
     * @return 数据集
     */
    List<GroupRoles> roles(Long groupId);

    /**
     * 查询关联 group
     *
     * @param roleId 主键
     * @return 数据集
     */
    List<GroupRoles> groups(Long roleId);

    /**
     * 保存用户-group关系
     *
     * @param groupId group主键
     * @param roleIds roleId集合
     * @return 结果集
     */
    List<GroupRoles> relation(Long groupId, Set<Long> roleIds);
}
