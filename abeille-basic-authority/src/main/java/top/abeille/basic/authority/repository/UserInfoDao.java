/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.abeille.basic.authority.entity.UserInfo;

/**
 * 用户信息(userInfo)dao接口
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
public interface UserInfoDao extends JpaRepository<UserInfo, Long> {

    /**
     * 根据username获取用户信息
     *
     * @param username 主键
     * @return UserInfo 用户信息
     */
    UserInfo getByUsername(String username);
}
