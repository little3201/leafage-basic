/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.authority.entity.RoleInfo;

/**
 * 角色信息dao接口
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
public interface RoleInfoDao extends JpaRepository<RoleInfo, Long> {
}
