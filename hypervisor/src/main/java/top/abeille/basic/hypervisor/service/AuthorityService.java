/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Flux;
import top.abeille.basic.hypervisor.dto.AuthorityDTO;
import top.abeille.basic.hypervisor.vo.AuthorityVO;
import top.abeille.basic.hypervisor.vo.CountVO;
import top.abeille.common.basic.BasicService;

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
     * @param codes code集合
     * @return 统计信息
     */
    Flux<CountVO> countRoles(Set<String> codes);
}
