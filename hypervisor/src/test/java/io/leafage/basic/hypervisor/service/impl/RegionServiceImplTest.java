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

import io.leafage.basic.hypervisor.domain.Region;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
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
 * region service test
 *
 * @author liwenqiang 2021-08-30 9:38
 **/
@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionServiceImpl regionService;

    private RegionDTO regionDTO;

    @BeforeEach
    void init() {
        regionDTO = new RegionDTO();
        regionDTO.setName("西安市");
        regionDTO.setAreaCode("029");
        regionDTO.setPostalCode(710000);
        regionDTO.setSuperiorId(1L);
    }

    @Test
    void retrieve() {
        given(this.regionRepository.findByEnabledTrue(Mockito.any(PageRequest.class))).willReturn(Flux.just(Mockito.mock(Region.class)));

        given(this.regionRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(regionService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.regionRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Region.class)));

        StepVerifier.create(regionService.fetch(Mockito.anyLong())).expectNextCount(1).verifyComplete();
    }

    @Test
    void subordinates() {
        given(this.regionRepository.findBySuperiorId(Mockito.anyLong())).willReturn(Flux.just(Mockito.mock(Region.class)));

        StepVerifier.create(regionService.subordinates(Mockito.anyLong())).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.regionRepository.existsByName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(regionService.exist("北京市")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        given(this.regionRepository.save(Mockito.any(Region.class))).willReturn(Mono.just(Mockito.mock(Region.class)));

        StepVerifier.create(regionService.create(Mockito.mock(RegionDTO.class))).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        given(this.regionRepository.findById(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Region.class)));

        given(this.regionRepository.save(Mockito.any(Region.class))).willReturn(Mono.just(Mockito.mock(Region.class)));

        StepVerifier.create(regionService.modify(Mockito.anyLong(), regionDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        given(this.regionRepository.deleteById(Mockito.anyLong())).willReturn(Mono.empty());

        StepVerifier.create(regionService.remove(Mockito.anyLong())).verifyComplete();
    }

}