/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.hypervisor.entity.UserRole;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
