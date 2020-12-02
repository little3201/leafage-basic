/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.AuthorityInfo;
import top.abeille.basic.hypervisor.dto.AuthorityDTO;
import top.abeille.basic.hypervisor.vo.AuthorityVO;
import top.abeille.common.basic.BasicService;

import java.util.List;

/**
 * 权限资源信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface AuthorityService extends BasicService<AuthorityDTO, AuthorityVO> {

    /**
     * 根据代码查询资源信息
     *
     * @param code 代码
     * @return 数据库映射对象
     */
    Mono<AuthorityInfo> findByCodeAndEnabledTrue(String code);

    /**
     * 根据主键批量查询资源信息
     *
     * @param sourceIdList 主键集合
     * @return 数据库映射对象
     */
    Flux<AuthorityInfo> findByIdInAndEnabledTrue(List<String> sourceIdList);
}
