package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.RolePrivileges;

import java.util.List;
import java.util.Set;

/**
 * role privileges service.
 *
 * @author liwenqiang 2021/9/27 14:18
 **/
public interface RolePrivilegesService {

    /**
     * 查询关联privilege
     *
     * @param roleId role主键
     * @return 数据集
     */
    List<RolePrivileges> privileges(Long roleId);

    /**
     * 查询关联role
     *
     * @param privilegeId privilege 主键
     * @return 数据集
     */
    List<RolePrivileges> roles(Long privilegeId);

    /**
     * 保存role-privilege关系
     *
     * @param roleId     role主键
     * @param privileges privilege集合
     * @return 结果集
     */
    List<RolePrivileges> relation(Long roleId, Set<Long> privileges);
}
