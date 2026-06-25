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

package top.leafage.hypervisor.system.service.impl;

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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.logging.annotation.OperationLog;
import top.leafage.hypervisor.system.domain.Dictionary;
import top.leafage.hypervisor.system.domain.dto.DictionaryDTO;
import top.leafage.hypervisor.system.domain.vo.DictionaryVO;
import top.leafage.hypervisor.system.repository.DictionaryRepository;
import top.leafage.hypervisor.system.service.DictionaryService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * dictionary service impl.
 *
 * @author wq li
 */
@OperationLog("dictionaries")
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private static final BeanCopier copier = BeanCopier.create(DictionaryDTO.class, Dictionary.class, false);
    private final DictionaryRepository dictionaryRepository;

    /**
     * Constructor for DictionaryServiceImpl.
     *
     * @param dictionaryRepository a {@link DictionaryRepository} object
     */
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<@NonNull DictionaryVO> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Specification<Dictionary> spec = (root, _, cb) -> {
            Optional<Predicate> predicate = buildPredicate(filters, cb, root);
            // 添加superiorId的条件
            Predicate basePredicate = predicate.orElse(cb.conjunction());
            if (StringUtils.hasText(filters) && filters.contains("superiorId")) {
                return basePredicate;
            } else {
                return cb.and(basePredicate, cb.isNull(root.get("superiorId")));
            }
        };

        Page<Dictionary> entityPage = dictionaryRepository.findAll(spec, pageable);
        if (entityPage.isEmpty()) {
            return Page.empty(pageable);
        }
        Set<Long> ids = entityPage.getContent().stream()
                .map(Dictionary::getId)
                .collect(Collectors.toSet());

        List<Object[]> countResults = dictionaryRepository.countBySuperiorIdsGrouped(ids);
        Map<Long, Long> countMap = countResults.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // superiorId
                        row -> (Long) row[1]   // count
                ));

        return entityPage.map(entity -> {
            long count = countMap.getOrDefault(entity.getId(), 0L);
            return DictionaryVO.from(entity, count);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DictionaryVO fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return dictionaryRepository.findById(id)
                .map(DictionaryVO::from)
                .orElseThrow(() -> new EntityNotFoundException("dictionary not found: " + id));
    }

    @Transactional
    @Override
    public boolean enable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!dictionaryRepository.existsById(id)) {
            throw new EntityNotFoundException("dictionary not found: " + id);
        }
        return dictionaryRepository.enableById(id) > 0;
    }

    @Transactional
    @Override
    public boolean disable(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        if (!dictionaryRepository.existsById(id)) {
            throw new EntityNotFoundException("dictionary not found: " + id);
        }
        return dictionaryRepository.disableById(id) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DictionaryVO> subset(Long id) {
        List<Dictionary> list;
        if (id == null) {
            list = dictionaryRepository.findAllBySuperiorIdIsNull();
        } else {
            list = dictionaryRepository.findAllBySuperiorId(id);
        }
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Set<Long> ids = list.stream()
                .map(Dictionary::getId)
                .collect(Collectors.toSet());

        List<Object[]> countResults = dictionaryRepository.countBySuperiorIdsGrouped(ids);
        Map<Long, Long> countMap = countResults.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // superiorId
                        row -> (Long) row[1]   // count
                ));

        return list.stream()
                .map(entity -> {
                    long count = countMap.getOrDefault(entity.getId(), 0L);
                    return DictionaryVO.from(entity, count);
                })
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public DictionaryVO create(DictionaryDTO dto) {
        if (dictionaryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }
        Dictionary entity = dictionaryRepository.save(DictionaryDTO.toEntity(dto));
        return DictionaryVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public DictionaryVO modify(Long id, DictionaryDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Dictionary existing = dictionaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("dictionary not found: " + id));
        if (!existing.getName().equals(dto.getName()) &&
                dictionaryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("name already exists: " + dto.getName());
        }

        copier.copy(dto, existing, null);
        Dictionary entity = dictionaryRepository.save(existing);
        return DictionaryVO.from(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (!dictionaryRepository.existsById(id)) {
            throw new EntityNotFoundException("dictionary not found: " + id);
        }
        dictionaryRepository.deleteById(id);
    }

}
