/*
 * Copyright (c) 2024-2026.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.assets.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.jspecify.annotations.NonNull;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.leafage.hypervisor.assets.domain.Region;
import top.leafage.hypervisor.assets.domain.dto.RegionDTO;
import top.leafage.hypervisor.assets.domain.vo.RegionVO;
import top.leafage.hypervisor.assets.repository.RegionRepository;
import top.leafage.hypervisor.assets.service.RegionService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * region service impl.
 *
 * @author wq li
 */
@Service
public class RegionServiceImpl implements RegionService {

    private static final BeanCopier copier = BeanCopier.create(RegionDTO.class, Region.class, false);
    private final RegionRepository regionRepository;

    /**
     * Constructor for RegionServiceImpl.
     *
     * @param regionRepository a {@link RegionRepository} object
     */
    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<@NonNull RegionVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Region> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            if (StringUtils.hasText(filters) && filters.contains("superiorId")) {
                return basePredicate;
            } else {
                return cb.and(basePredicate, cb.isNull(root.get("superiorId")));
            }
        };

        return regionRepository.findAll(spec, pageable)
                .map(entity -> {
                    long count = regionRepository.countBySuperiorId(entity.getId());
                    return RegionVO.from(entity, count);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegionVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return regionRepository.findById(id)
                .map(RegionVO::from)
                .orElseThrow(() -> new EntityNotFoundException("region not found: " + id));
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!regionRepository.existsById(id)) {
            throw new EntityNotFoundException("region not found: " + id);
        }
        return regionRepository.updateEnabledById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RegionVO> subset(Long id) {
        List<Region> list;
        if (id == null) {
            list = regionRepository.findAllBySuperiorIdIsNull();
        } else {
            list = regionRepository.findAllBySuperiorId(id);
        }
        return list.stream().sorted(Comparator.comparing(Region::getId))
                .map(entity -> {
                    long count = regionRepository.countBySuperiorId(entity.getId());
                    return RegionVO.from(entity, count);
                })
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public RegionVO create(RegionDTO dto) {
        if (regionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        Region entity = regionRepository.save(RegionDTO.toEntity(dto));
        return RegionVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public RegionVO modify(Long id, RegionDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Region existing = regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("region not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                regionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        copier.copy(dto, existing, null);
        Region entity = regionRepository.save(existing);
        return RegionVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!regionRepository.existsById(id)) {
            throw new EntityNotFoundException("region not found: " + id);
        }
        regionRepository.deleteById(id);
    }

}
