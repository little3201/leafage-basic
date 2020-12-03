/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.Group;

/**
 * 组织信息dao
 *
 * @author liwenqiang 2018/12/20 9:52
 **/
@Repository
public interface GroupRepository extends ReactiveMongoRepository<Group, String> {

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 组织信息
     */
    Mono<Group> findByCodeAndEnabledTrue(String code);
}
