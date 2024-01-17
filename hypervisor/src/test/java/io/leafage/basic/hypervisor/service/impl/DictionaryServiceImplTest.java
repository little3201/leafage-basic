/*
 *  Copyright 2018-2024 the original author or authors.
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

/**
 * dictionary service test
 *
 * @author liwenqiang 2022-04-8 7:45
 **/
@ExtendWith(MockitoExtension.class)
class DictionaryServiceImplTest {

    @Mock
    private DictionaryRepository dictionaryRepository;

    @InjectMocks
    private DictionaryServiceImpl dictionaryService;

    @Test
    void retrieve() {
        given(this.dictionaryRepository.findByEnabledTrue(Mockito.any(PageRequest.class)))
                .willReturn(Flux.just(Mockito.mock(Dictionary.class)));

        given(this.dictionaryRepository.count()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(dictionaryService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.dictionaryRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Dictionary.class)));

        StepVerifier.create(dictionaryService.fetch(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void superior() {
        given(this.dictionaryRepository.findBySuperiorIdIsNull()).willReturn(Flux.just(Mockito.mock(Dictionary.class)));

        StepVerifier.create(dictionaryService.superior()).expectNextCount(1).verifyComplete();
    }

    @Test
    void subordinates() {
        given(this.dictionaryRepository.findBySuperiorId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(Dictionary.class)));

        StepVerifier.create(dictionaryService.subordinates(1L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.dictionaryRepository.save(Mockito.any(Dictionary.class))).willReturn(Mono.just(Mockito.mock(Dictionary.class)));

        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("Gender");
        dictionaryDTO.setDescription("描述");
        StepVerifier.create(dictionaryService.create(dictionaryDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.dictionaryRepository.existsByName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(dictionaryService.exist("vip")).expectNext(Boolean.TRUE).verifyComplete();
    }
}