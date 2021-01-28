/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.RoleDTO;
import io.leafage.basic.hypervisor.vo.CountVO;
import io.leafage.basic.hypervisor.vo.RoleVO;
import io.leafage.common.basic.BasicService;
import reactor.core.publisher.Flux;

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
