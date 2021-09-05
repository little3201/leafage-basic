/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.vo.RegionVO;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * region service
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public interface RegionService extends ReactiveBasicService<RegionDTO, RegionVO> {

    /**
     * 根据code查询
     *
     * @param code 代码
     * @return 查询结果
     */
    Mono<RegionVO> fetch(long code);
}
