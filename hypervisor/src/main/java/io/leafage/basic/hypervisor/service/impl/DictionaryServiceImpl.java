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

import io.leafage.basic.hypervisor.domain.Dictionary;
import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.repository.DictionaryRepository;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

import java.util.NoSuchElementException;

/**
 * dictionary service impl
 *
 * @author wq li
 */
@Service
public class DictionaryServiceImpl extends ReactiveAbstractTreeNodeService<Dictionary> implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    /**
     * <p>Constructor for DictionaryServiceImpl.</p>
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
    public Mono<Page<DictionaryVO>> retrieve(int page, int size, String sortBy, boolean descending) {
        Pageable pageable = pageable(page, size, sortBy, descending);

        Flux<DictionaryVO> voFlux = dictionaryRepository.findAllBy(pageable)
                .map(d -> convertToVO(d, DictionaryVO.class));

        Mono<Long> count = dictionaryRepository.count();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<DictionaryVO> subset(Long id) {
        Assert.notNull(id, "id must not be null.");
        return dictionaryRepository.findBySuperiorId(id).map(d -> convertToVO(d, DictionaryVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<DictionaryVO> fetch(Long id) {
        Assert.notNull(id, "id must not be null.");
        return dictionaryRepository.findById(id).map(d -> convertToVO(d, DictionaryVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> exists(String name, Long id) {
        Assert.hasText(name, "name must not be empty.");
        return dictionaryRepository.existsByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<DictionaryVO> create(DictionaryDTO dto) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dto, dictionary);
        return dictionaryRepository.save(dictionary).map(d -> convertToVO(d, DictionaryVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<DictionaryVO> modify(Long id, DictionaryDTO dto) {
        Assert.notNull(id, "id must not be null.");
        return dictionaryRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .map(dictionary -> convert(dto, dictionary))
                .flatMap(dictionaryRepository::save)
                .map(d -> convertToVO(d, DictionaryVO.class));
    }

}
