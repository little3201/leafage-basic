package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.DictionaryDTO;
import io.leafage.basic.hypervisor.entity.Dictionary;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * dictionary controller test
 *
 * @author liwenqiang 2022-04-07 9:19
 **/
@ExtendWith(MockitoExtension.class)
class DictionaryServiceImplTest {


    @Mock
    private DictionaryRepository dictionaryRepository;

    @InjectMocks
    private DictionaryServiceImpl dictionaryService;

    @Test
    void retrieve() {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode("2247J1IK");
        dictionary.setName("Gender");
        dictionary.setAlias("性别");
        dictionary.setSuperior("2247J0IL");
        Page<Dictionary> regions = new PageImpl<>(List.of(dictionary));
        given(this.dictionaryRepository.findAll(PageRequest.of(0, 2))).willReturn(regions);

        Page<DictionaryVO> voPage = dictionaryService.retrieve(0, 2);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.dictionaryRepository.getByCodeAndEnabledTrue(Mockito.anyString())).willReturn(Mockito.mock(Dictionary.class));

        DictionaryVO dictionaryVO = dictionaryService.fetch("2247J0IL");

        Assertions.assertNotNull(dictionaryVO);
    }

    @Test
    void lower() {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode("2247J1IK");
        dictionary.setName("Gender");
        dictionary.setAlias("性别");
        dictionary.setSuperior("2247J0IL");
        given(this.dictionaryRepository.findBySuperiorAndEnabledTrue(Mockito.anyString())).willReturn(List.of(dictionary));

        List<DictionaryVO> dictionaryVOS = dictionaryService.lower("2247JD0K");

        Assertions.assertNotNull(dictionaryVOS);
    }

    @Test
    void lower_empty() {
        List<DictionaryVO> dictionaryVOS = dictionaryService.lower("2247JD0K");

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

        DictionaryDTO dictionaryDTO = new DictionaryDTO();
        dictionaryDTO.setName("性别");
        dictionaryDTO.setDescription("描述");
        DictionaryVO dictionaryVO = dictionaryService.create(dictionaryDTO);

        verify(this.dictionaryRepository, times(1)).saveAndFlush(Mockito.any(Dictionary.class));
        Assertions.assertNotNull(dictionaryVO);
    }

}