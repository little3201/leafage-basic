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

    Mono<ResourceInfo> findByCodeAndEnabledTrue(String code);

    Flux<ResourceInfo> findByIdInAndEnabledTrue(List<String> sourceIdList);
}
