/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import top.abeille.basic.hypervisor.entity.UserGroup;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface UserGroupRepository extends ReactiveCrudRepository<UserGroup, String> {

    /**
     * 查询所有角色——根据用户id
     *
     * @param userId 用户ID
     * @return List
     */
    List<UserGroup> findAllByUserIdAndEnabled(@NotNull Long userId, Boolean enabled);
}
