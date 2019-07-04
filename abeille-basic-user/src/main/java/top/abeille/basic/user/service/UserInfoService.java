/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.user.service;

import top.abeille.basic.user.entity.UserInfo;
import top.abeille.common.basic.BasicService;

/**
 * java类描述
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserInfoService extends BasicService<UserInfo> {

    /**
     * 根据username获取用户信息
     *
     * @param username 主键
     * @return UserInfo 用户信息
     */
    UserInfo getByUsername(String username);

    void removeByUserId(String userId);

    UserInfo getByUserId(String userId);
}
