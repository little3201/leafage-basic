package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.vo.RoleVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Set;

/**
 * role authority service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface RoleAuthorityService {

    /**
     * 查询关联权限
     *
     * @param code 角色代码
     * @return 数据集
     */
    Mono<List<String>> authorities(String code);

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
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(String code, Set<String> authorities);
}
