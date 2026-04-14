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
import top.leafage.common.data.domain.TreeNode;
import top.leafage.hypervisor.assets.domain.ReportSection;
import top.leafage.hypervisor.assets.domain.SchemaSection;
import top.leafage.hypervisor.assets.domain.Section;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.repository.ReportSectionRepository;
import top.leafage.hypervisor.assets.repository.SchemaSectionRepository;
import top.leafage.hypervisor.assets.repository.SectionRepository;
import top.leafage.hypervisor.assets.service.SectionService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static top.leafage.common.data.converter.ModelToTreeNodeConverter.toTree;

@Service
public class SectionServiceImpl implements SectionService {

    private static final BeanCopier copier = BeanCopier.create(SectionDTO.class, Section.class, false);

    private final SectionRepository sectionRepository;
    private final ReportSectionRepository reportSectionRepository;
    private final SchemaSectionRepository schemaSectionRepository;

    /**
     * Constructor for SectionServiceImpl.
     *
     * @param sectionRepository a {@link SectionRepository} object
     */
    public SectionServiceImpl(SectionRepository sectionRepository, ReportSectionRepository reportSectionRepository, SchemaSectionRepository schemaSectionRepository) {
        this.sectionRepository = sectionRepository;
        this.reportSectionRepository = reportSectionRepository;
        this.schemaSectionRepository = schemaSectionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<@NonNull SectionVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Section> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            // 添加superiorId的条件
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            if (StringUtils.hasText(filters) && filters.contains("superiorId")) {
                return basePredicate;
            } else {
                return cb.and(basePredicate, cb.isNull(root.get("superiorId")));
            }
        };

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

    @Override
    public List<TreeNode<Long>> reportTree(Long reportId) {
        Assert.notNull(reportId, String.format(_MUST_NOT_BE_NULL, "reportId"));

        List<ReportSection> sections = reportSectionRepository.findAllByReportId(reportId);
        return toTree(sections);
    }

    @Override
    public List<TreeNode<Long>> schemaTree(Long schemaId) {
        Assert.notNull(schemaId, String.format(_MUST_NOT_BE_NULL, "schemaId"));

        List<SchemaSection> sections = schemaSectionRepository.findAllBySchemaId(schemaId);
        return toTree(sections);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SectionVO> subset(Long id) {
        List<Section> list;
        if (id == null) {
            list = sectionRepository.findAllBySuperiorIdIsNull();
        } else {
            list = sectionRepository.findAllBySuperiorId(id);
        }
        return list.stream().sorted(Comparator.comparing(Section::getId))
                .map(entity -> {
                    long count = sectionRepository.countBySuperiorId(entity.getId());
                    return SectionVO.from(entity, count);
                })
                .toList();
    }

    @Override
    public SectionVO createReportSection(Long reportId, SectionDTO dto) {
        Section section = SectionDTO.toEntity(dto);
        ReportSection reportSection = new ReportSection(reportId, section);

        ReportSection entity = reportSectionRepository.save(reportSection);
        return SectionVO.from(entity);
    }

    @Override
    public SectionVO createSchemaSection(Long schemaId, SectionDTO dto) {
        Section section = SectionDTO.toEntity(dto);
        SchemaSection schemaSection = new SchemaSection(schemaId, section);

        SchemaSection entity = schemaSectionRepository.save(schemaSection);
        return SectionVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SectionVO create(SectionDTO dto) {
        if (sectionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("title already exists: " + dto.getName());
        }
        Section entity = sectionRepository.save(SectionDTO.toEntity(dto));
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
        if (!existing.getName().equals(dto.getName()) &&
                sectionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("title already exists: " + dto.getName());
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
