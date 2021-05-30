package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import reactor.core.publisher.Flux;

public interface UserRoleService {

    /**
     * 查询关联用户
     *
     * @param code 代码
     * @return 数据集
     */
    Flux<UserVO> roleRelation(String code);

    /**
     * 查询关联角色
     *
     * @param username 账号
     * @return 数据集
     */
    Flux<RoleVO> userRelation(String username);
}
