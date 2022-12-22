/*
 *  Copyright 2018-2022 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.bo.SimpleBO;
import io.leafage.basic.hypervisor.document.Region;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.ValidMessage;

import java.util.NoSuchElementException;

/**
 * region service impl
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
@Service
public class RegionServiceImpl implements RegionService {

    private static final String CODE_MESSAGE = "code must not null";

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Mono<Page<RegionVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<RegionVO> voFlux = regionRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = regionRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<RegionVO> fetch(Long code) {
        Assert.notNull(code, CODE_MESSAGE);
        return regionRepository.getByCodeAndEnabledTrue(code).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return regionRepository.existsByName(name);
    }

    @Override
    public Flux<RegionVO> lower(long code) {
        return regionRepository.findBySuperiorAndEnabledTrue(code).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RegionVO> create(RegionDTO regionDTO) {
        return Mono.just(regionDTO).map(dto -> {
                    Region region = new Region();
                    BeanUtils.copyProperties(regionDTO, region);
                    return region;
                })
                .flatMap(regionRepository::insert).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RegionVO> modify(Long code, RegionDTO regionDTO) {
        Assert.notNull(code, ValidMessage.CODE_NOT_NULL);
        return regionRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(region -> BeanUtils.copyProperties(regionDTO, region))
                .flatMap(regionRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(Long code) {
        Assert.notNull(code, ValidMessage.CODE_NOT_NULL);
        return regionRepository.getByCodeAndEnabledTrue(code).flatMap(group ->
                regionRepository.deleteById(group.getId()));
    }

    /**
     * 数据转换
     *
     * @param region 信息
     * @return RegionVO 输出对象
     */
    private Mono<RegionVO> convertOuter(Region region) {
        return Mono.just(region).map(r -> {
            RegionVO vo = new RegionVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).flatMap(vo -> {
            if (region.getSuperior() != null) {
                // 存在上级，则查询
                return regionRepository.findById(region.getSuperior()).map(superior -> {
                    SimpleBO<Long> basicVO = new SimpleBO<>();
                    BeanUtils.copyProperties(superior, basicVO);
                    vo.setSuperior(basicVO);
                    return vo;
                }).switchIfEmpty(Mono.just(vo));
            }
            return Mono.just(vo);
        });
    }
}
