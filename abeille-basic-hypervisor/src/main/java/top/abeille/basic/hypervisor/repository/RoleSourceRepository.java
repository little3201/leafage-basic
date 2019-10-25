/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.hypervisor.entity.RoleSource;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色权限dao接口
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
@Repository
public interface RoleSourceRepository extends ReactiveCrudRepository<RoleSource, String> {

    /**
     * 查询所有资源——根据角色id
     *
     * @param roleId 角色ID
     * @return List
     */
    List<RoleSource> findAllByRoleIdAndEnabled(@NotNull Long roleId, Boolean enabled);
}
