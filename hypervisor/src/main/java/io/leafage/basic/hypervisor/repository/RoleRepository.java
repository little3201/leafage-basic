/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Role;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

/**
 * 角色repository
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
@Repository
public interface RoleRepository extends ReactiveMongoRepository<Role, ObjectId> {

    /**
     * 分页查询有效角色
     *
     * @param pageable 分页参数
     * @return 数据集
     */
    Flux<Role> findByEnabledTrue(Pageable pageable);

    /**
     * 查询有效角色
     *
     * @return 数据集
     */
    Flux<Role> findByEnabledTrue();

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 角色信息
     */
    Mono<Role> getByCodeAndEnabledTrue(@NotBlank String code);

    /**
     * 查询角色
     *
     * @param codes 代码集合
     * @return 角色信息
     */
    Flux<Role> findByCodeInAndEnabledTrue(@NotEmpty Collection<String> codes);

    /**
     * 是否已存在
     *
     * @param name 名称
     * @return true-是，false-否
     */
    Mono<Boolean> existsByName(String name);
}
