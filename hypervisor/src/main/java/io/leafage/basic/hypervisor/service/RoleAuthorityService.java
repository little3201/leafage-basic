package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.document.RoleAuthority;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface RoleAuthorityService {

    /**
     * 查询关联权限
     *
     * @param code 角色代码
     * @return 数据集
     */
    Flux<AuthorityVO> authorities(String code);

    /**
     * 查询关联角色
     *
     * @param code 权限代码
     * @return 数据集
     */
    Flux<RoleVO> roles(String code);

    /**
     * 保存角色-权限关系
     *
     * @param code        角色代码
     * @param authorities 权限信息
     * @return 结果集
     */
    Flux<RoleAuthority> relation(String code, Set<String> authorities);
}
