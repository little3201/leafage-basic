package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import java.util.List;
import java.util.Set;

public interface UserRoleService {

    /**
     * 查询关联权限
     *
     * @param code 角色代码
     * @return 数据集
     */
    List<UserVO> users(String code);

    /**
     * 查询关联角色
     *
     * @param username 账号
     * @return 数据集
     */
    List<RoleVO> roles(String username);

    /**
     * 保存角色-权限关系
     *
     * @param username 账号
     * @param roles    角色信息
     * @return 结果集
     */
    List<UserRole> relation(String username, Set<String> roles);
}
