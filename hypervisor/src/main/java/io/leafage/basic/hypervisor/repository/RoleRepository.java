/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * role repository.
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根据code查询
     *
     * @param code 唯一标识
     * @return 信息
     */
    Role getByCodeAndEnabledTrue(String code);

    /**
     * 查询
     *
     * @return 信息
     */
    List<Role> findByEnabledTrue();
}
