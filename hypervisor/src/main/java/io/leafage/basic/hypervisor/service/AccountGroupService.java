package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.vo.AccountVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Set;

public interface AccountGroupService {

    /**
     * 查询关联账号
     *
     * @param code 代码
     * @return 数据集
     */
    Flux<AccountVO> accounts(String code);

    /**
     * 查询关联分组
     *
     * @param code 代码
     * @return 数据集
     */
    Mono<List<String>> groups(String code);

    /**
     * 保存用户-分组关系
     *
     * @param username 用户
     * @param groups   分组信息
     * @return 是否成功： true - 是， false - 否
     */
    Mono<Boolean> relation(String username, Set<String> groups);
}