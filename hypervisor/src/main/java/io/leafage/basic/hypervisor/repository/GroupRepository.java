/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Group;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 组织信息dao
 *
 * @author liwenqiang 2018/12/20 9:52
 **/
@Repository
public interface GroupRepository extends ReactiveMongoRepository<Group, ObjectId> {

    /**
     * 分页查询组
     *
     * @param pageable 分页参数
     * @return 有效组
     */
    Flux<Group> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 组织信息
     */
    Mono<Group> getByCodeAndEnabledTrue(String code);

    /**
     * 根据id查询name
     *
     * @param id 主键
     * @return 组织信息
     */
    @Query(value = "{ 'id' : ?0 }", fields = "{ 'name' : 1}")
    Mono<Group> getById(ObjectId id);
}
