/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.Role;

/**
 * 角色信息dao接口
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
@Repository
public interface RoleRepository extends ReactiveMongoRepository<Role, String> {

    /**
     * 分页查询角色
     *
     * @param pageable 分页参数
     * @return 有效角色
     */
    Flux<Role> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 角色信息
     */
    Mono<Role> findByCodeAndEnabledTrue(String code);
}
