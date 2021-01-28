/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 角色信息dao接口
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
    Role findByCodeAndEnabledTrue(String code);

    /**
     * 根据codes查询
     *
     * @param codes 唯一标识
     * @return 信息
     */
    List<Role> findByCodeInAndEnabledTrue(Collection<String> codes);
}
