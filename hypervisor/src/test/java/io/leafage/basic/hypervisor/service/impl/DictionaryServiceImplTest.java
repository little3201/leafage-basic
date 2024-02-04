package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.Dictionary;
import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.repository.DictionaryRepository;
import io.leafage.basic.hypervisor.vo.DictionaryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * dictionary controller test
 *
 * @author wq li 2022-04-07 9:19
 **/
@ExtendWith(MockitoExtension.class)
class DictionaryServiceImplTest {


    @Mock
    private DictionaryRepository dictionaryRepository;

    @InjectMocks
    private DictionaryServiceImpl dictionaryService;

    @Test
    void retrieve() {
        Page<Dictionary> regions = new PageImpl<>(List.of(Mockito.mock(Dictionary.class)));
        given(this.dictionaryRepository.findAll(PageRequest.of(0, 2))).willReturn(regions);

        Page<DictionaryVO> voPage = dictionaryService.retrieve(0, 2);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.dictionaryRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(Mockito.mock(Dictionary.class)));

        DictionaryVO dictionaryVO = dictionaryService.fetch(1L);

        Assertions.assertNotNull(dictionaryVO);
    }

    @Test
    void lower() {
        given(this.dictionaryRepository.findBySuperiorIdAndEnabledTrue(Mockito.anyLong())).willReturn(List.of(Mockito.mock(Dictionary.class)));

        List<DictionaryVO> dictionaryVOS = dictionaryService.lower(1L);

        Assertions.assertNotNull(dictionaryVOS);
    }

    @Test
    void lower_empty() {
        given(this.dictionaryRepository.findBySuperiorIdAndEnabledTrue(Mockito.anyLong())).willReturn(Collections.emptyList());

        List<DictionaryVO> dictionaryVOS = dictionaryService.lower(1L);

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

}