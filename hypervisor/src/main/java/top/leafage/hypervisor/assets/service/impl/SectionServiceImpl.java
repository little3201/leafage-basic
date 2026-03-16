/*
 * Copyright (c) 2026.  little3201.
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
import org.jspecify.annotations.NonNull;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.leafage.hypervisor.assets.domain.Section;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.repository.SectionRepository;
import top.leafage.hypervisor.assets.service.SectionService;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    private static final BeanCopier copier = BeanCopier.create(SectionDTO.class, Section.class, false);
    private final SectionRepository sectionRepository;

    /**
     * Constructor for SectionServiceImpl.
     *
     * @param sectionRepository a {@link SectionRepository} object
     */
    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<@NonNull SectionVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<@NonNull Section> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);
        if (!StringUtils.hasText(filters) || !filters.contains("superiorId")) {
            spec = spec.and((root, _, cb) -> cb.isNull(root.get("superiorId")));
        }

        return sectionRepository.findAll(spec, pageable)
                .map(entity -> {
                    long count = sectionRepository.countBySuperiorId(entity.getId());
                    return SectionVO.from(entity, count);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SectionVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return sectionRepository.findById(id)
                .map(SectionVO::from)
                .orElseThrow(() -> new EntityNotFoundException("section not found: " + id));
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!sectionRepository.existsById(id)) {
            throw new EntityNotFoundException("section not found: " + id);
        }
        return sectionRepository.updateEnabledById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SectionVO> subset(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return sectionRepository.findAllBySuperiorId(id)
                .stream().map(entity -> {
                    long count = sectionRepository.countBySuperiorId(entity.getId());
                    return SectionVO.from(entity, count);
                })
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SectionVO create(SectionDTO dto) {
        if (sectionRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("title already exists: " + dto.getTitle());
        }
        Section entity = sectionRepository.saveAndFlush(SectionDTO.toEntity(dto));
        return SectionVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SectionVO modify(Long id, SectionDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Section existing = sectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("section not found: " + id));
        if (!existing.getTitle().equals(dto.getTitle()) &&
                sectionRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("title already exists: " + dto.getTitle());
        }
        copier.copy(dto, existing, null);
        Section entity = sectionRepository.save(existing);
        return SectionVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!sectionRepository.existsById(id)) {
            throw new EntityNotFoundException("section not found: " + id);
        }
        sectionRepository.deleteById(id);
    }

}
