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

import io.leafage.basic.hypervisor.bo.SimpleBO;
import io.leafage.basic.hypervisor.document.Region;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.repository.RegionRepository;
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
 * region接口测试
 *
 * @author liwenqiang 2021/8/30 9:38
 **/
@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionServiceImpl regionService;

    @Test
    void retrieve() {
        given(this.regionRepository.findByEnabledTrue(Mockito.any(Pageable.class))).willReturn(Flux.just(Mockito.mock(Region.class)));

        given(this.regionRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(regionService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Region region = new Region();
        region.setId(new ObjectId());
        region.setCode(2L);
        region.setName("北京市");
        region.setAlias("京");
        region.setSuperior(new ObjectId());
        region.setPostalCode(23423080);
        region.setDescription("描述");
        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(region));

        given(this.regionRepository.findById(Mockito.any(ObjectId.class)))
                .willReturn(Mono.just(Mockito.mock(Region.class)));

        StepVerifier.create(regionService.fetch(1100L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void lower() {
        Region region = new Region();
        region.setId(new ObjectId());
        region.setCode(2L);
        region.setName("北京市");
        region.setAlias("京");
        region.setPostalCode(23423080);
        region.setDescription("描述");
        given(this.regionRepository.findBySuperiorAndEnabledTrue(Mockito.anyLong()))
                .willReturn(Flux.just(region));

        StepVerifier.create(regionService.lower(11L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void exist() {
        given(this.regionRepository.existsByName(Mockito.anyString())).willReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(regionService.exist("北京市")).expectNext(Boolean.TRUE).verifyComplete();
    }

    @Test
    void create() {
        given(this.regionRepository.insert(Mockito.any(Region.class))).willReturn(Mono.just(Mockito.mock(Region.class)));

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("西安市");
        regionDTO.setAlias("秦");
        regionDTO.setAreaCode("029");
        regionDTO.setPostalCode(710000);
        regionDTO.setDescription("描述信息");

        SimpleBO<Long> partBO = new SimpleBO<>();
        partBO.setCode(2L);
        partBO.setName("Test");
        regionDTO.setSuperior(partBO);
        StepVerifier.create(regionService.create(regionDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create_error() {
        given(this.regionRepository.insert(Mockito.any(Region.class))).willThrow(new RuntimeException());

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("测试村");

        SimpleBO<Long> partBO = new SimpleBO<>();
        partBO.setCode(2L);
        partBO.setName("Test");
        regionDTO.setSuperior(partBO);
        StepVerifier.create(regionService.create(regionDTO)).expectError(RuntimeException.class).verify();
    }

    @Test
    void modify() {
        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Region.class)));

        Region region = new Region();
        region.setId(new ObjectId());
        region.setSuperior(new ObjectId());
        given(this.regionRepository.save(Mockito.any(Region.class))).willReturn(Mono.just(region));

        given(this.regionRepository.findById(Mockito.any(ObjectId.class)))
                .willReturn(Mono.just(Mockito.mock(Region.class)));

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("测试村");
        region.setAlias("Test");

        SimpleBO<Long> partBO = new SimpleBO<>();
        partBO.setCode(2L);
        partBO.setName("Test");
        regionDTO.setSuperior(partBO);
        StepVerifier.create(regionService.modify(11L, regionDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void remove() {
        Region region = new Region();
        region.setId(new ObjectId());
        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(region));

        given(this.regionRepository.deleteById(Mockito.any(ObjectId.class))).willReturn(Mono.empty());

        StepVerifier.create(regionService.remove(11L)).verifyComplete();
    }

}