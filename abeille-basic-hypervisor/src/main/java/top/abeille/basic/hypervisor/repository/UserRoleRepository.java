/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.hypervisor.entity.UserRole;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, String> {

    /**
     * 查询所有角色——根据用户id
     *
     * @param userId 用户ID
     * @return List
     */
    List<UserRole> findAllByUserIdAndEnabled(@NotNull Long userId, Boolean enabled);
}
