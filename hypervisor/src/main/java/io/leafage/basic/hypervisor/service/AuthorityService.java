/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import io.leafage.basic.hypervisor.vo.CountVO;
import io.leafage.common.basic.BasicService;
import reactor.core.publisher.Flux;

import java.util.Set;

/**
 * 权限资源信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends BasicService<AuthorityDTO, AuthorityVO> {

    /**
     * 统计关联信息
     *
     * @param ids ID集合
     * @return 统计信息
     */
    Flux<CountVO> countRelations(Set<String> ids);
}
