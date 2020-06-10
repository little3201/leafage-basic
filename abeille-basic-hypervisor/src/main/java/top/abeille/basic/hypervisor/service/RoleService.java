/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.RoleInfo;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.BasicService;

import java.util.ArrayList;

/**
 * 角色信息service 接口
 *
 * @author liwenqiang 2018/9/27 14:18
 **/
public interface RoleService extends BasicService<RoleDTO, RoleVO> {

    /**
     * 根据业务ID查询数据库信息，返回数据库映射对象
     *
     * @param businessId 业务ID
     * @return 数据库映射对象
     */
    Mono<RoleInfo> fetchInfo(String businessId);

    /**
     * 根据用户业务ID查询权限信息，返回角色业务ID集合
     *
     * @param businessId 用户业务ID
     * @return 权限业务ID集合
     */
    Mono<ArrayList<String>> retrieveRoles(String businessId);
}
