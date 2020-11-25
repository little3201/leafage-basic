/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.document.UserRole;

import javax.validation.constraints.NotNull;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, String> {

    /**
     * 根据用户id查询关联角色
     *
     * @param userId 用户ID
     * @return 关联的角色
     */
    Flux<UserRole> findByUserId(@NotNull String userId);
}
