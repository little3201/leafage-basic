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
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import top.leafage.common.data.domain.TreeNode;
import top.leafage.hypervisor.assets.domain.*;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.dto.SectionFieldDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionFieldVO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.repository.*;
import top.leafage.hypervisor.assets.service.SectionService;

import java.util.Comparator;
import java.util.List;

import static top.leafage.common.data.converter.ModelToTreeNodeConverter.toTree;

@Service
public class SectionServiceImpl implements SectionService {

    private static final BeanCopier copier = BeanCopier.create(SectionDTO.class, Section.class, false);

    private final SectionRepository sectionRepository;
    private final ArchiveSectionRepository archiveSectionRepository;
    private final SchemaSectionRepository schemaSectionRepository;
    private final ReportSectionRepository reportSectionRepository;
    private final SectionFieldRepository sectionFieldRepository;

    /**
     * Constructor for SectionServiceImpl.
     *
     * @param sectionRepository        a {@link SectionRepository} object
     * @param archiveSectionRepository a {@link ArchiveSectionRepository} object
     * @param schemaSectionRepository  a {@link SchemaSectionRepository} object
     * @param reportSectionRepository  a {@link ReportSectionRepository} object
     * @param sectionFieldRepository   a {@link SectionFieldRepository} object
     */
    public SectionServiceImpl(SectionRepository sectionRepository, ArchiveSectionRepository archiveSectionRepository,
                              SchemaSectionRepository schemaSectionRepository, ReportSectionRepository reportSectionRepository,
                              SectionFieldRepository sectionFieldRepository) {
        this.sectionRepository = sectionRepository;
        this.archiveSectionRepository = archiveSectionRepository;
        this.schemaSectionRepository = schemaSectionRepository;
        this.reportSectionRepository = reportSectionRepository;
        this.sectionFieldRepository = sectionFieldRepository;
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
    public List<TreeNode<Long>> archiveTree(Long archiveId) {
        Assert.notNull(archiveId, String.format(_MUST_NOT_BE_NULL, "archiveId"));

        List<ArchiveSection> sections = archiveSectionRepository.findAllByArchiveId(archiveId);
        return toTree(sections);
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
        Assert.notNull(reportId, String.format(_MUST_NOT_BE_NULL, "reportId"));

        Section section = SectionDTO.toEntity(dto);
        ReportSection reportSection = new ReportSection(reportId, section);

        ReportSection entity = reportSectionRepository.save(reportSection);
        return SectionVO.from(entity);
    }

    @Override
    public SectionVO createSchemaSection(Long schemaId, SectionDTO dto) {
        Assert.notNull(schemaId, String.format(_MUST_NOT_BE_NULL, "schemaId"));

        Section section = SectionDTO.toEntity(dto);
        SchemaSection schemaSection = new SchemaSection(schemaId, section);

        SchemaSection entity = schemaSectionRepository.save(schemaSection);
        return SectionVO.from(entity);
    }

    @Override
    public SectionVO createArchiveSection(Long archiveId, SectionDTO dto) {
        Assert.notNull(archiveId, String.format(_MUST_NOT_BE_NULL, "archiveId"));

        Section section = SectionDTO.toEntity(dto);
        ArchiveSection archiveSection = new ArchiveSection(archiveId, section);

        ArchiveSection entity = archiveSectionRepository.save(archiveSection);
        return SectionVO.from(entity);
    }

    @Override
    public List<SectionFieldVO> fields(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return sectionFieldRepository.findAllBySectionId(id)
                .stream().map(SectionFieldVO::from)
                .toList();
    }

    @Override
    public SectionFieldVO createField(SectionFieldDTO dto) {
        if (sectionFieldRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        SectionField entity = sectionFieldRepository.save(SectionFieldDTO.toEntity(dto));
        return SectionFieldVO.from(entity);
    }

    @Override
    public SectionFieldVO modifyField(Long id, SectionFieldDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        SectionField existing = sectionFieldRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("section field not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                sectionFieldRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        copier.copy(dto, existing, null);
        SectionField entity = sectionFieldRepository.save(existing);
        return SectionFieldVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SectionVO create(SectionDTO dto) {
        if (sectionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
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
            throw new IllegalArgumentException("name already exists: " + dto.getName());
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
