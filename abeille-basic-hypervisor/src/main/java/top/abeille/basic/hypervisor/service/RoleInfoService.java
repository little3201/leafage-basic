/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import top.abeille.basic.hypervisor.entity.RoleInfo;
import top.abeille.common.basic.BasicService;

import java.util.List;

/**
 * 角色信息service 接口
 *
 * @author liwenqiang 2018/9/27 14:18
 **/
public interface RoleInfoService extends BasicService<RoleInfo> {

    /**
     * 根据用户ID查询相关角色信息
     *
     * @param userId 用户ID
     * @return RoleInfoVO
     */
    List<RoleInfo> findByUserId(String userId);
}
