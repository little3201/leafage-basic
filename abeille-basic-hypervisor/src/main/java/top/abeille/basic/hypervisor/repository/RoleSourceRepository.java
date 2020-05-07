/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.document.RoleSource;

import javax.validation.constraints.NotNull;

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
    Flux<RoleSource> findAllByRoleIdAndEnabled(@NotNull String roleId, boolean enabled);
}
