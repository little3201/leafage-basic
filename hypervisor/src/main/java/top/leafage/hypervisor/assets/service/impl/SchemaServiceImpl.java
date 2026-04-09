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
import top.leafage.hypervisor.assets.domain.Schema;
import top.leafage.hypervisor.assets.domain.SchemaSection;
import top.leafage.hypervisor.assets.domain.Section;
import top.leafage.hypervisor.assets.domain.dto.SchemaDTO;
import top.leafage.hypervisor.assets.domain.vo.SchemaVO;
import top.leafage.hypervisor.assets.domain.vo.SectionVO;
import top.leafage.hypervisor.assets.repository.SchemaRepository;
import top.leafage.hypervisor.assets.repository.SchemaSectionRepository;
import top.leafage.hypervisor.assets.service.SchemaService;

import java.util.Comparator;
import java.util.List;

@Service
public class SchemaServiceImpl implements SchemaService {

    private static final BeanCopier copier = BeanCopier.create(SchemaDTO.class, Schema.class, false);

    private final SchemaRepository schemaRepository;
    private final SchemaSectionRepository schemaSectionRepository;

    /**
     * Constructor for SchemaServiceImpl.
     *
     * @param schemaRepository a {@link SchemaRepository} object
     */
    public SchemaServiceImpl(SchemaRepository schemaRepository, SchemaSectionRepository schemaSectionRepository) {
        this.schemaRepository = schemaRepository;
        this.schemaSectionRepository = schemaSectionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<@NonNull SchemaVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<@NonNull Schema> spec = (root, _, cb) ->
                buildPredicate(filters, cb, root).orElse(null);

        return schemaRepository.findAll(spec, pageable)
                .map(SchemaVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchemaVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return schemaRepository.findById(id)
                .map(SchemaVO::from)
                .orElseThrow(() -> new EntityNotFoundException("schema not found: " + id));
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!schemaRepository.existsById(id)) {
            throw new EntityNotFoundException("schema not found: " + id);
        }
        return schemaRepository.updateEnabledById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SchemaVO create(SchemaDTO dto) {
        if (schemaRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        Schema entity = schemaRepository.saveAndFlush(SchemaDTO.toEntity(dto));
        return SchemaVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SchemaVO modify(Long id, SchemaDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Schema existing = schemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("schema not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                schemaRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        copier.copy(dto, existing, null);
        Schema entity = schemaRepository.save(existing);
        return SchemaVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!schemaRepository.existsById(id)) {
            throw new EntityNotFoundException("schema not found: " + id);
        }
        schemaRepository.deleteById(id);
    }

    @Override
    public Page<SectionVO> sections(Long id, int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<SchemaSection> spec = (root, query, cb) -> {
            Predicate predicate = buildPredicate(filters, cb, root).orElse(null);
            // 添加schemaId的条件
            Predicate schemaIdPredicate = cb.ge(root.get("schemaId"), id);
            return predicate == null ? schemaIdPredicate : cb.and(predicate, schemaIdPredicate);
        };

        return schemaSectionRepository.findAll(spec, pageable)
                .map(SectionVO::from);
    }

    @Override
    public List<SectionVO> subset(Long id, Long sectionId) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        List<SchemaSection> list;
        if (sectionId == null) {
            list = schemaSectionRepository.findAllBySchemaIdAndSuperiorIdIsNull(id);
        } else {
            list = schemaSectionRepository.findAllBySchemaIdAndSuperiorId(id, sectionId);
        }
        return list.stream().sorted(Comparator.comparing(Section::getId))
                .map(entity -> {
                    long count = schemaSectionRepository.countBySchemaIdAndSuperiorId(id, entity.getId());
                    return SectionVO.from(entity, count);
                })
                .toList();
    }
}
