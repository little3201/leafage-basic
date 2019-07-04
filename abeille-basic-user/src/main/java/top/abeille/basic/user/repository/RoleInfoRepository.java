/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.user.entity.RoleInfo;

/**
 * 角色信息dao接口
 *
 * @author liwenqiang 2018/9/26 11:06
 **/
public interface RoleInfoRepository extends JpaRepository<RoleInfo, Long> {
}
