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
import top.leafage.hypervisor.assets.domain.Archive;
import top.leafage.hypervisor.assets.domain.ArchiveSection;
import top.leafage.hypervisor.assets.domain.dto.ArchiveDTO;
import top.leafage.hypervisor.assets.domain.vo.ArchiveVO;
import top.leafage.hypervisor.assets.repository.ArchiveRepository;
import top.leafage.hypervisor.assets.repository.ArchiveSectionRepository;
import top.leafage.hypervisor.assets.repository.SchemaSectionRepository;
import top.leafage.hypervisor.assets.service.ArchiveService;
import top.leafage.hypervisor.assets.service.ArchiveService;

import java.util.List;

@Service
public class ArchiveServiceImpl implements ArchiveService {

    private static final BeanCopier copier = BeanCopier.create(ArchiveDTO.class, Archive.class, false);

    private final ArchiveRepository archiveRepository;
    private final ArchiveSectionRepository archiveSectionRepository;
    private final SchemaSectionRepository schemaSectionRepository;

    /**
     * Constructor for ArchiveServiceImpl.
     *
     * @param archiveRepository a {@link ArchiveRepository} object
     */
    public ArchiveServiceImpl(ArchiveRepository archiveRepository, ArchiveSectionRepository archiveSectionRepository,
                              SchemaSectionRepository schemaSectionRepository) {
        this.archiveRepository = archiveRepository;
        this.archiveSectionRepository = archiveSectionRepository;
        this.schemaSectionRepository = schemaSectionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<@NonNull ArchiveVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<@NonNull Archive> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);

        return archiveRepository.findAll(spec, pageable)
                .map(ArchiveVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArchiveVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return archiveRepository.findById(id)
                .map(ArchiveVO::from)
                .orElseThrow(() -> new EntityNotFoundException("archive not found: " + id));
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!archiveRepository.existsById(id)) {
            throw new EntityNotFoundException("archive not found: " + id);
        }
        return archiveRepository.updateEnabledById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ArchiveVO create(ArchiveDTO dto) {
        if (archiveRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("title already exists: " + dto.getTitle());
        }
        Archive entity = archiveRepository.save(ArchiveDTO.toEntity(dto));
        // 执行模板内容复制
        Long schemaId = dto.getSchemaId();
        if (schemaId != null) {
            List<ArchiveSection> sections = schemaSectionRepository.findAllBySchemaId(schemaId)
                    .stream().map(schemaSection -> ArchiveSection.from(entity.getId(), schemaSection))
                    .toList();
            archiveSectionRepository.saveAll(sections);
        }
        return ArchiveVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ArchiveVO modify(Long id, ArchiveDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Archive existing = archiveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("archive not found: " + id));
        if (!existing.getTitle().equals(dto.getTitle()) &&
                archiveRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("title already exists: " + dto.getTitle());
        }
        copier.copy(dto, existing, null);
        Archive entity = archiveRepository.save(existing);
        return ArchiveVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!archiveRepository.existsById(id)) {
            throw new EntityNotFoundException("archive not found: " + id);
        }
        archiveRepository.deleteById(id);
    }

}
