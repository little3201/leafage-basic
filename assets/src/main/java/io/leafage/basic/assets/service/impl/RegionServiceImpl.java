/*
 *  Copyright 2018-2024 little3201.
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

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Region;
import io.leafage.basic.assets.dto.RegionDTO;
import io.leafage.basic.assets.repository.RegionRepository;
import io.leafage.basic.assets.service.RegionService;
import io.leafage.basic.assets.vo.RegionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

/**
 * region service impl
 *
 * @author wq li
 */
@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    /**
     * <p>Constructor for RegionServiceImpl.</p>
     *
     * @param regionRepository a {@link io.leafage.basic.assets.repository.RegionRepository} object
     */
    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<RegionVO>> retrieve(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Flux<RegionVO> voFlux = regionRepository.findByEnabledTrue(pageable).flatMap(this::convertOuter);

        Mono<Long> count = regionRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /** {@inheritDoc} */
    @Override
    public Mono<RegionVO> fetch(Long id) {
        Assert.notNull(id, "region id must not be null.");
        return regionRepository.findById(id).flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, "region name must not be blank.");
        return regionRepository.existsByName(name);
    }

    /** {@inheritDoc} */
    @Override
    public Flux<RegionVO> subordinates(Long superiorId) {
        Assert.notNull(superiorId, "region superior id must not be null.");
        return regionRepository.findBySuperiorId(superiorId).flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<RegionVO> create(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        return regionRepository.save(region).flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<RegionVO> modify(Long id, RegionDTO regionDTO) {
        Assert.notNull(id, "region id must not be null.");
        return regionRepository.findById(id).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(region -> BeanUtils.copyProperties(regionDTO, region))
                .flatMap(regionRepository::save).flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "region id must not be null.");
        return regionRepository.deleteById(id);
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
            vo.setLastModifiedDate(r.getLastModifiedDate().orElse(null));
            return vo;
        });
    }
}
