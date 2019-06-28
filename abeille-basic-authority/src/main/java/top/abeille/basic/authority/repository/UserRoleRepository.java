/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.authority.entity.UserRole;

/**
 * 用户角色Dao
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
