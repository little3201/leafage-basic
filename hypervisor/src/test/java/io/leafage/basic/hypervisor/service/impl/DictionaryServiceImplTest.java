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
import io.leafage.basic.hypervisor.vo.DictionaryVO;
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
 * dictionary controller test
 *
 * @author wq li
 **/
@ExtendWith(MockitoExtension.class)
class DictionaryServiceImplTest {


    @Mock
    private DictionaryRepository dictionaryRepository;

    @InjectMocks
    private DictionaryServiceImpl dictionaryService;

    private DictionaryDTO dictionaryDTO;

    @BeforeEach
    void init() {
        dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("group");
    }

    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Page<Dictionary> regions = new PageImpl<>(List.of(Mockito.mock(Dictionary.class)));
        given(this.dictionaryRepository.findBySuperiorIdIsNull(pageable)).willReturn(regions);

        Page<DictionaryVO> voPage = dictionaryService.retrieve(0, 2, "id", true);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.dictionaryRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Dictionary.class)));

        DictionaryVO dictionaryVO = dictionaryService.fetch(1L);

        Assertions.assertNotNull(dictionaryVO);
    }

    @Test
    void subset() {
        given(this.dictionaryRepository.findBySuperiorId(Mockito.anyLong())).willReturn(List.of(Mockito.mock(Dictionary.class)));

        List<DictionaryVO> dictionaryVOS = dictionaryService.subset(1L);

        Assertions.assertNotNull(dictionaryVOS);
    }

    @Test
    void lower_empty() {
        given(this.dictionaryRepository.findBySuperiorId(Mockito.anyLong())).willReturn(Collections.emptyList());

        List<DictionaryVO> dictionaryVOS = dictionaryService.subset(1L);

        Assertions.assertEquals(Collections.emptyList(), dictionaryVOS);
    }

    @Test
    void exist() {
        given(this.dictionaryRepository.existsByName(Mockito.anyString())).willReturn(true);

        boolean exist = dictionaryService.exist("性别");

        Assertions.assertTrue(exist);
    }

    @Test
    void create() {
        given(this.dictionaryRepository.saveAndFlush(Mockito.any(Dictionary.class))).willReturn(Mockito.mock(Dictionary.class));

        DictionaryVO dictionaryVO = dictionaryService.create(Mockito.mock(DictionaryDTO.class));

        verify(this.dictionaryRepository, times(1)).saveAndFlush(Mockito.any(Dictionary.class));
        Assertions.assertNotNull(dictionaryVO);
    }

    @Test
    void modify() {
        given(this.dictionaryRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Dictionary.class)));

        given(this.dictionaryRepository.save(Mockito.any(Dictionary.class))).willReturn(Mockito.mock(Dictionary.class));

        DictionaryVO dictionaryVO = dictionaryService.modify(1L, dictionaryDTO);

        verify(this.dictionaryRepository, times(1)).save(Mockito.any(Dictionary.class));
        Assertions.assertNotNull(dictionaryVO);
    }

    @Test
    void remove() {
        dictionaryService.remove(1L);

        verify(this.dictionaryRepository, times(1)).deleteById(Mockito.anyLong());
    }
}