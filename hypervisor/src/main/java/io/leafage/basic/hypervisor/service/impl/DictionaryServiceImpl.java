/*
 *  Copyright 2018-2023 the original author or authors.
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

import io.leafage.basic.hypervisor.document.Dictionary;
import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.repository.DictionaryRepository;
import io.leafage.basic.hypervisor.service.DictionaryService;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.ValidMessage;
import top.leafage.common.reactive.ReactiveAbstractTreeNodeService;

/**
 * dictionary service impl
 *
 * @author liwenqiang 2022-03-30 07:34
 **/
@Service
public class DictionaryServiceImpl extends ReactiveAbstractTreeNodeService<Dictionary> implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Mono<Page<DictionaryVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Flux<DictionaryVO> voFlux = dictionaryRepository.findByEnabledTrue(pageRequest).map(this::convertOuter);

        Mono<Long> count = dictionaryRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Flux<DictionaryVO> superior() {
        return dictionaryRepository.findBySuperiorIsNullAndEnabledTrue().map(this::convertOuter);
    }

    @Override
    public Flux<DictionaryVO> lower(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return dictionaryRepository.findBySuperiorAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Mono<DictionaryVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return dictionaryRepository.getByCodeAndEnabledTrue(code).map(this::convertOuter);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        Assert.hasText(name, ValidMessage.NAME_NOT_BLANK);
        return dictionaryRepository.existsByName(name);
    }

    @Override
    public Mono<DictionaryVO> create(DictionaryDTO dictionaryDTO) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDTO, dictionary);
        dictionary.setCode(this.generateCode());
        return dictionaryRepository.insert(dictionary).map(this::convertOuter);
    }

    private DictionaryVO convertOuter(Dictionary dictionary) {
        DictionaryVO vo = new DictionaryVO();
        BeanUtils.copyProperties(dictionary, vo);
        return vo;
    }
}
