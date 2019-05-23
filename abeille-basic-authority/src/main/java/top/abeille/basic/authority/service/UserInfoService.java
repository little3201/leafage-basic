/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service;

import top.abeille.basic.authority.model.UserInfoModel;
import top.abeille.common.basic.BasicService;

/**
 * java类描述
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserInfoService extends BasicService<UserInfoModel> {

    /**
     * 根据username获取用户信息
     *
     * @param username 主键
     * @return UserInfoModel 用户信息
     */
    UserInfoModel getByUsername(String username);
}
