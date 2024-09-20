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

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Region;
import io.leafage.basic.assets.dto.RegionDTO;
import io.leafage.basic.assets.repository.RegionRepository;
import io.leafage.basic.assets.vo.RegionVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * region service test
 *
 * @author wq li 2021/12/7 15:125
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
        regionDTO.setPostalCode(71000);
        regionDTO.setSuperiorId(1L);
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Page<Region> regions = new PageImpl<>(List.of(Mockito.mock(Region.class)));
        given(this.regionRepository.findAll(pageable)).willReturn(regions);

        Page<RegionVO> voPage = regionService.retrieve(0, 2, "id", true);
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.regionRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Region.class)));

        RegionVO regionVO = regionService.fetch(Mockito.anyLong());

        Assertions.assertNotNull(regionVO);
    }

    @Test
    void subset() {
        given(this.regionRepository.findBySuperiorId(Mockito.anyLong())).willReturn(List.of(Mockito.mock(Region.class)));

        List<RegionVO> regionVOS = regionService.subset(Mockito.anyLong());

        Assertions.assertNotNull(regionVOS);
    }

    @Test
    void lower_empty() {
        List<RegionVO> regionVOS = regionService.subset(Mockito.anyLong());

        Assertions.assertEquals(Collections.emptyList(), regionVOS);
    }

    @Test
    void exist() {
        given(this.regionRepository.existsByName(Mockito.anyString())).willReturn(true);

        boolean exist = regionService.exist("成都市");

        Assertions.assertTrue(exist);
    }

    @Test
    void create() {
        given(this.regionRepository.saveAndFlush(Mockito.any(Region.class))).willReturn(Mockito.mock(Region.class));

        RegionVO regionVO = regionService.create(Mockito.mock(RegionDTO.class));

        verify(this.regionRepository, times(1)).saveAndFlush(Mockito.any(Region.class));
        Assertions.assertNotNull(regionVO);
    }

    @Test
    void modify() {
        given(this.regionRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Region.class)));

        given(this.regionRepository.save(Mockito.any(Region.class))).willReturn(Mockito.mock(Region.class));

        RegionVO regionVO = regionService.modify(Mockito.anyLong(), regionDTO);

        verify(this.regionRepository, times(1)).save(Mockito.any(Region.class));
        Assertions.assertNotNull(regionVO);
    }

    @Test
    void remove() {
        regionService.remove(11L);

        verify(this.regionRepository, times(1)).deleteById(Mockito.anyLong());
    }
}