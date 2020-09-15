/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.SourceInfo;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.BasicService;

import java.util.List;

/**
 * 权限资源信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface SourceService extends BasicService<SourceDTO, SourceVO> {

    /**
     * 根据业务ID查询资源信息
     *
     * @param businessId 业务ID
     * @return 数据库映射对象
     */
    Mono<SourceInfo> fetchInfo(String businessId);

    /**
     * 根据主键批量查询资源信息
     *
     * @param sourceIdList 主键集合
     * @return 数据库映射对象
     */
    Flux<SourceInfo> findByIdInAndEnabledTrue(List<String> sourceIdList);
}
