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

package top.leafage.hypervisor.system.service.impl;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.hypervisor.system.domain.Dictionary;
import top.leafage.hypervisor.system.domain.dto.DictionaryDTO;
import top.leafage.hypervisor.system.domain.vo.DictionaryVO;
import top.leafage.hypervisor.system.repository.DictionaryRepository;
import top.leafage.hypervisor.system.service.DictionaryService;

import java.util.NoSuchElementException;

/**
 * dictionary service impl
 *
 * @author wq li
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    private static final BeanCopier copier = BeanCopier.create(DictionaryDTO.class, Dictionary.class, false);
    private final DictionaryRepository dictionaryRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    /**
     * Constructor for DictionaryServiceImpl.
     *
     * @param dictionaryRepository a {@link DictionaryRepository} object
     */
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.dictionaryRepository = dictionaryRepository;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<DictionaryVO>> retrieve(int page, int size, String sortBy, boolean descending, String filters) {
        Pageable pageable = pageable(page, size, sortBy, descending);
        Criteria criteria = buildCriteria(filters, Dictionary.class);
        if (!StringUtils.hasText(filters) || !filters.contains("superiorId")) {
            criteria = criteria.and("superiorId").isNull();
        }

        return r2dbcEntityTemplate.select(Dictionary.class)
                .matching(Query.query(criteria).with(pageable))
                .all()
                .flatMapSequential(entity -> dictionaryRepository.countBySuperiorId(entity.getId())
                        .map(count -> DictionaryVO.from(entity, count)))
                .collectList()
                .zipWith(r2dbcEntityTemplate.count(Query.query(criteria), Dictionary.class))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<DictionaryVO> subset(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return dictionaryRepository.findBySuperiorId(id)
                .flatMap(entity -> dictionaryRepository.countBySuperiorId(entity.getId())
                        .map(count -> DictionaryVO.from(entity, count)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<DictionaryVO> fetch(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return dictionaryRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .map(DictionaryVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Mono<DictionaryVO> create(DictionaryDTO dto) {
        return dictionaryRepository.existsByName(dto.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("name already exists: " + dto.getName()));
                    }
                    return dictionaryRepository.save(DictionaryDTO.toEntity(dto))
                            .map(DictionaryVO::from);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Mono<DictionaryVO> modify(Long id, DictionaryDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return dictionaryRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(existing -> {
                    if (!existing.getName().equals(dto.getName())) {
                        return dictionaryRepository.existsByName(dto.getName())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new IllegalArgumentException("dictionary name already exists: " + dto.getName()));
                                    }
                                    // Copy the DTO to the existing entity if names differ
                                    copier.copy(dto, existing, null);
                                    return dictionaryRepository.save(existing);
                                });
                    } else {
                        // If the names are the same, no need to check the database
                        copier.copy(dto, existing, null);
                        return dictionaryRepository.save(existing);
                    }
                })
                .map(DictionaryVO::from);
    }

    @Transactional
    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return dictionaryRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NoSuchElementException("dictionary not found: " + id));
                    }
                    return dictionaryRepository.deleteById(id);
                });
    }
}
