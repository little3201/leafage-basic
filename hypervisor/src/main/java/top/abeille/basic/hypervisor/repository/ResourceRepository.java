/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.ResourceInfo;

import java.util.List;

/**
 * 权限资源dao
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface ResourceRepository extends ReactiveMongoRepository<ResourceInfo, String> {

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 资源信息
     */
    Mono<ResourceInfo> findByCodeAndEnabledTrue(String code);

    /**
     * 根据权限Id集合查询多条enabled信息
     *
     * @param sourceIdList id集合
     * @return 资源信息
     */
    Flux<ResourceInfo> findByIdInAndEnabledTrue(List<String> sourceIdList);
}