package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.document.UserRole;
import io.leafage.basic.hypervisor.vo.UserVO;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UserRoleService {

    /**
     * 查询关联用户
     *
     * @param code 代码
     * @return 数据集
     */
    Flux<UserVO> users(String code);

    /**
     * 查询关联角色
     *
     * @param username 账号
     * @return 数据集
     */
    Flux<String> roles(String username);

    /**
     * 保存用户-角色关系
     *
     * @param username 用户
     * @param roles    角色信息
     * @return 结果集
     */
    Flux<UserRole> relation(String username, Set<String> roles);
}
