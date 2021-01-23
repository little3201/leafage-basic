/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.vo.CountVO;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.BasicService;

import java.util.Set;

/**
 * 组织信息Service
 *
 * @author liwenqiang 2018/12/17 19:24
 **/
public interface GroupService extends BasicService<GroupDTO, GroupVO> {

    /**
     * 统计关联信息
     *
     * @param codes code集合
     * @return 统计信息
     */
    Flux<CountVO> countUsers(Set<String> codes);
}
