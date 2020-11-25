/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.RoleInfo;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.BasicService;

/**
 * 角色信息service 接口
 *
 * @author liwenqiang 2018/9/27 14:18
 **/
public interface RoleService extends BasicService<RoleDTO, RoleVO> {

    /**
     * 根据code查询关联角色
     *
     * @param code 代码
     * @return 角色信息
     */
    Mono<RoleInfo> findByCodeAndEnabledTrue(String code);
}
