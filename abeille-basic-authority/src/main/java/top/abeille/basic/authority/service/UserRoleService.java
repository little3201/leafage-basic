/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service;

import top.abeille.basic.authority.model.UserRoleModel;
import top.abeille.common.basic.BasicService;

import java.util.List;

/**
 * 用户角色Service
 *
 * @author liwenqiang 2018-12-06 22:04
 **/
public interface UserRoleService extends BasicService<UserRoleModel> {

    /**
     * 查询所有角色——根据用户id
     *
     * @param userId 用户ID
     * @return List
     */
    List<UserRoleModel> findAllByUserId(Long userId);
}
