/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.user.entity.RoleSource;

/**
 * 角色权限dao接口
 *
 * @author liwenqiang 2018/9/26 11:29
 **/
public interface RoleSourceRepository extends JpaRepository<RoleSource, Long> {
}
