/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.SourceInfo;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.BasicService;

/**
 * 权限资源信息Service
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
public interface SourceService extends BasicService<SourceDTO, SourceVO> {

    /**
     * 根据业务ID查询数据库信息，返回数据库映射对象
     *
     * @param businessId 业务ID
     * @return 数据库映射对象
     */
    Mono<SourceInfo> fetchInfo(String businessId);

}
