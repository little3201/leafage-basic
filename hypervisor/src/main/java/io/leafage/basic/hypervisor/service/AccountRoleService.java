package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.vo.AccountVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * account role service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccountRoleService {

    /**
     * 查询关联账号
     *
     * @param code 代码
     * @return 数据集
     */
    Flux<AccountVO> accounts(String code);

    /**
     * 查询关联角色
     *
     * @param username 账号
     * @return 数据集
     */
    Mono<List<String>> roles(String username);

    /**
     * 保存用户-角色关系
     *
     * @param username 用户
     * @param roles    角色信息
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(String username, Set<String> roles);
}
