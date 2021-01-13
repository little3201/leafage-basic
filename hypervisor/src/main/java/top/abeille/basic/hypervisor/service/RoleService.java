/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.dto.RoleDTO;
import top.abeille.basic.hypervisor.vo.CountVO;
import top.abeille.basic.hypervisor.vo.RoleVO;
import top.abeille.common.basic.BasicService;

import java.util.Set;

/**
 * 角色信息service 接口
 *
 * @author liwenqiang 2018/9/27 14:18
 **/
public interface RoleService extends BasicService<RoleDTO, RoleVO> {

    /**
     * 统计关联信息
     *
     * @param ids ID集合
     * @return 统计信息
     */
    Flux<CountVO> countRelations(Set<String> ids);
}
