/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.document.UserGroup;

import javax.validation.constraints.NotNull;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface UserGroupRepository extends ReactiveCrudRepository<UserGroup, String> {

    /**
     * 查询所有角色——根据用户id
     *
     * @param userId 用户ID
     * @return List
     */
    Flux<UserGroup> findByUserId(@NotNull Long userId);
}
