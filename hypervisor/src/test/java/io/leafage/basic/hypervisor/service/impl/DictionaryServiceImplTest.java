/*
 *  Copyright 2018-2022 the original author or authors.
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
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * dictionary service test
 *
 * @author liwenqiang 2022/4/8 7:45
 **/
@ExtendWith(MockitoExtension.class)
class DictionaryServiceImplTest {

    @Mock
    private DictionaryRepository dictionaryRepository;

    @InjectMocks
    private DictionaryServiceImpl dictionaryService;

    @Test
    void retrieve() {
        given(this.dictionaryRepository.findByEnabledTrue(Mockito.any(Pageable.class)))
                .willReturn(Flux.just(Mockito.mock(Dictionary.class)));

        given(this.dictionaryRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(dictionaryService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Dictionary dictionary = new Dictionary();
        dictionary.setId(new ObjectId());
        dictionary.setCode("2247KS91");
        dictionary.setName("Gender");
        dictionary.setAlias("性别");
        dictionary.setDescription("描述");
        given(this.dictionaryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mono.just(dictionary));

        StepVerifier.create(dictionaryService.fetch("2247KS91")).expectNextCount(1).verifyComplete();
    }

    @Test
    void superior() {
        Dictionary dictionary = new Dictionary();
        dictionary.setId(new ObjectId());
        dictionary.setCode("2247KS91");
        dictionary.setName("Gender");
        dictionary.setAlias("性别");
        dictionary.setDescription("描述");
        given(this.dictionaryRepository.findBySuperiorIsNullAndEnabledTrue()).willReturn(Flux.just(dictionary));

        StepVerifier.create(dictionaryService.superior()).expectNextCount(1).verifyComplete();
    }

    @Test
    void lower() {
        Dictionary dictionary = new Dictionary();
        dictionary.setId(new ObjectId());
        dictionary.setCode("2247KS91");
        dictionary.setName("Gender");
        dictionary.setAlias("性别");
        dictionary.setDescription("描述");
        given(this.dictionaryRepository.findBySuperiorAndEnabledTrue(Mockito.anyString()))
                .willReturn(Flux.just(dictionary));

        StepVerifier.create(dictionaryService.lower("2247KS91")).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.dictionaryRepository.insert(Mockito.any(Dictionary.class))).willReturn(Mono.just(Mockito.mock(Dictionary.class)));

        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("Gender");
        dictionaryDTO.setAlias("性别");
        dictionaryDTO.setDescription("描述");
        StepVerifier.create(dictionaryService.create(dictionaryDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.dictionaryRepository.existsByName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(dictionaryService.exist("vip")).expectNext(Boolean.TRUE).verifyComplete();
    }
}