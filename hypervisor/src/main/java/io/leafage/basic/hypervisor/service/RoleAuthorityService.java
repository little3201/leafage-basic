package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.entity.RoleAuthority;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import java.util.List;
import java.util.Set;

public interface RoleAuthorityService {

    /**
     * 查询关联权限
     *
     * @param code 角色代码
     * @return 数据集
     */
    List<AuthorityVO> authorities(String code);

    /**
     * 查询关联角色
     *
     * @param code 权限代码
     * @return 数据集
     */
    List<RoleVO> roles(String code);

    /**
     * 保存角色-权限关系
     *
     * @param code        角色代码
     * @param authorities 权限信息
     * @return 结果集
     */
    List<RoleAuthority> relation(String code, Set<String> authorities);
}
