/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.vo.enter.RoleEnter;
import top.abeille.basic.hypervisor.vo.outer.RoleOuter;
import top.abeille.common.basic.BasicService;

/**
 * 角色信息service 接口
 *
 * @author liwenqiang 2018/9/27 14:18
 **/
public interface RoleInfoService extends BasicService<RoleEnter, RoleOuter> {

    /**
     * 根据roleId获取角色信息
     *
     * @param roleId 业务主键
     * @return RoleOuter 角色信息
     */
    Mono<RoleOuter> getByRoleId(Long roleId);
}
