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

package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Region;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * region service impl.
 *
 * @author wq li 2021/11/27 14:18
 **/
@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Page<RegionVO> retrieve(int page, int size) {
        return regionRepository.findAll(PageRequest.of(page, size)).map(this::convert);
    }

    @Override
    public List<RegionVO> subset(Long id) {
        Assert.notNull(id, "region id must not be null.");
        return regionRepository.findBySuperiorId(id)
                .stream().map(this::convert).toList();
    }

    @Override
    public RegionVO fetch(Long id) {
        Assert.notNull(id, "region id must not be null.");
        Region region = regionRepository.findById(id).orElse(null);
        if (region == null) {
            return null;
        }
        return this.convert(region);
    }

    @Override
    public boolean exist(String name) {
        Assert.hasText(name, "region name must not bu blank.");
        return regionRepository.existsByName(name);
    }

    @Override
    public RegionVO create(RegionDTO dto) {
        Region region = new Region();
        BeanCopier copier = BeanCopier.create(RegionDTO.class, Region.class, false);
        copier.copy(dto, region, null);

        regionRepository.saveAndFlush(region);
        return this.convert(region);
    }

    @Override
    public RegionVO modify(Long id, RegionDTO dto) {
        Assert.notNull(id, "region id must not be null.");
        Region region = regionRepository.findById(id).orElse(null);
        if (region == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(RegionDTO.class, Region.class, false);
        copier.copy(dto, region, null);

        regionRepository.save(region);
        return this.convert(region);
    }

    @Override
    public void remove(Long id) {
        Assert.notNull(id, "region id must not be null.");
        regionRepository.deleteById(id);
    }

    /**
     * 数据转换
     *
     * @param region 信息
     * @return RegionVO 输出对象
     */
    private RegionVO convert(Region region) {
        RegionVO vo = new RegionVO();
        BeanCopier copier = BeanCopier.create(Region.class, RegionVO.class, false);
        copier.copy(region, vo, null);

        // get lastModifiedDate
        Optional<Instant> optionalInstant = region.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);
        return vo;
    }
}
