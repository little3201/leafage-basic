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
import top.leafage.hypervisor.assets.domain.Section;
import top.leafage.hypervisor.assets.domain.SectionData;
import top.leafage.hypervisor.assets.domain.SectionField;
import top.leafage.hypervisor.assets.domain.dto.SectionDTO;
import top.leafage.hypervisor.assets.domain.dto.SectionDataDTO;
import top.leafage.hypervisor.assets.domain.dto.SectionFieldDTO;
import top.leafage.hypervisor.assets.domain.vo.SectionDataVO;
import top.leafage.hypervisor.assets.domain.vo.SectionFieldVO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.repository.SectionDataRepository;
import top.leafage.hypervisor.assets.repository.SectionFieldRepository;
import top.leafage.hypervisor.assets.repository.SectionRepository;
import top.leafage.hypervisor.assets.service.SectionService;

import java.util.List;
import java.util.Set;

import static top.leafage.common.data.converter.ModelToTreeNodeConverter.toTree;

@Service
public class SectionServiceImpl implements SectionService {

    private static final Set<String> META_FIELDS = Set.of("sequence", "level");

    private static final BeanCopier copier = BeanCopier.create(SectionDTO.class, Section.class, false);
    private static final BeanCopier fieldCopier = BeanCopier.create(SectionFieldDTO.class, SectionField.class, false);
    private static final BeanCopier dataCopier = BeanCopier.create(SectionDataDTO.class, SectionData.class, false);

    private final SectionRepository sectionRepository;
    private final SectionFieldRepository sectionFieldRepository;
    private final SectionDataRepository sectionDataRepository;

    /**
     * Constructor for SectionServiceImpl.
     *
     * @param sectionRepository      a {@link SectionRepository} object
     * @param sectionFieldRepository a {@link SectionFieldRepository} object
     * @param sectionDataRepository  a {@link SectionDataRepository} object
     */
    public SectionServiceImpl(SectionRepository sectionRepository, SectionFieldRepository sectionFieldRepository, SectionDataRepository sectionDataRepository) {
        this.sectionRepository = sectionRepository;
        this.sectionFieldRepository = sectionFieldRepository;
        this.sectionDataRepository = sectionDataRepository;
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
    public List<TreeNode<Long>> tree(Long ownerId, String ownerType) {
        Assert.notNull(ownerId, String.format(_MUST_NOT_BE_NULL, "ownerId"));
        Assert.notNull(ownerType, String.format(_MUST_NOT_BE_NULL, "ownerType"));

        List<Section> sections = sectionRepository.findAllByOwnerIdAndOwnerType(ownerId, Section.OwnerType.of(ownerType));
        return toTree(sections, META_FIELDS);
    }

    @Override
    public SectionVO create(SectionDTO dto) {
        if (sectionRepository.existsByOwnerIdAndName(dto.getOwnerId(), dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        Section entity = sectionRepository.save(SectionDTO.toEntity(dto));
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
    public List<SectionDataVO> datas(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return sectionDataRepository.findAllBySectionId(id)
                .stream().map(SectionDataVO::from)
                .toList();
    }

    @Override
    public SectionFieldVO createField(SectionFieldDTO dto) {
        if (sectionFieldRepository.existsBySectionIdAndName(dto.getSectionId(), dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        SectionField entity = sectionFieldRepository.save(SectionFieldDTO.toEntity(dto));
        return SectionFieldVO.from(entity);
    }

    @Override
    public SectionDataVO createData(SectionDataDTO dto) {
        SectionData entity = sectionDataRepository.save(SectionDataDTO.toEntity(dto));
        return SectionDataVO.from(entity);
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
                sectionRepository.existsByOwnerIdAndName(existing.getOwnerId(), dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        copier.copy(dto, existing, null);
        Section entity = sectionRepository.save(existing);
        return SectionVO.from(entity);
    }

    @Override
    public SectionFieldVO modifyField(Long id, SectionFieldDTO dto) {
        Assert.notNull(id, String.format(_MUST_NOT_BE_NULL, "section field id"));

        SectionField existing = sectionFieldRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("section field not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                sectionFieldRepository.existsBySectionIdAndName(dto.getSectionId(), dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        fieldCopier.copy(dto, existing, null);
        SectionField entity = sectionFieldRepository.save(existing);
        return SectionFieldVO.from(entity);
    }

    @Override
    public SectionDataVO modifyData(Long id, SectionDataDTO dto) {
        Assert.notNull(id, String.format(_MUST_NOT_BE_NULL, "section data id"));

        SectionData existing = sectionDataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("section data not found: " + id));

        dataCopier.copy(dto, existing, null);
        SectionData entity = sectionDataRepository.save(existing);
        return SectionDataVO.from(entity);
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

    @Override
    public void removeField(Long id) {
        Assert.notNull(id, String.format(_MUST_NOT_BE_NULL, "section field id"));

        if (!sectionFieldRepository.existsById(id)) {
            throw new EntityNotFoundException("section not found: " + id);
        }
        sectionFieldRepository.deleteById(id);
    }

    @Override
    public void removeData(Long id) {
        Assert.notNull(id, String.format(_MUST_NOT_BE_NULL, "section field id"));

        if (!sectionDataRepository.existsById(id)) {
            throw new EntityNotFoundException("section not found: " + id);
        }
        sectionDataRepository.deleteById(id);
    }
}
