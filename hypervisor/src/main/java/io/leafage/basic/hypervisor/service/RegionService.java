/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * region service
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
public interface RegionService extends ReactiveBasicService<RegionDTO, RegionVO, Long> {

    /**
     * 获取下级
     *
     * @return 数据集
     */
    Flux<RegionVO> lower(long code);

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 结果集
     */
    Mono<Page<RegionVO>> retrieve(int page, int size);
}
