/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.RoleInfo;

/**
 * 角色信息dao接口
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
@Repository
public interface RoleRepository extends ReactiveMongoRepository<RoleInfo, String> {

    Mono<RoleInfo> findByCodeAndEnabledTrue(String code);
}
