package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.domain.Region;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import io.leafage.basic.hypervisor.vo.RegionVO;
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
 * region service test
 *
 * @author liwenqiang 2021/12/7 15:125
 **/
@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionServiceImpl regionService;

    @Test
    void retrieve() {
        Region region = new Region();
        region.setCode(1101L);
        region.setName("广东省");
        region.setAlias("粤");
        region.setSuperior(1L);
        Page<Region> regions = new PageImpl<>(List.of(region));
        given(this.regionRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(regions);

        Region superior = new Region();
        superior.setCode(region.getSuperior());
        superior.setName("北京市");
        given(this.regionRepository.findById(Mockito.anyLong())).willReturn(Optional.of(superior));

        Page<RegionVO> voPage = regionService.retrieve(0, 2);

        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void fetch() {
        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(Region.class));

        RegionVO regionVO = regionService.fetch(11L);

        Assertions.assertNotNull(regionVO);
    }

    @Test
    void lower() {
        Region region = new Region();
        region.setCode(1101L);
        region.setName("广东省");
        region.setAlias("粤");
        region.setSuperior(1L);
        given(this.regionRepository.findBySuperiorAndEnabledTrue(Mockito.anyLong())).willReturn(List.of(region));

        List<RegionVO> regionVOS = regionService.lower(11L);

        Assertions.assertNotNull(regionVOS);
    }

    @Test
    void lower_empty() {
        List<RegionVO> regionVOS = regionService.lower(110101001001L);

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

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("test");
        regionDTO.setAreaCode("23234");
        regionDTO.setDescription("描述");
        RegionVO regionVO = regionService.create(regionDTO);

        verify(this.regionRepository, times(1)).saveAndFlush(Mockito.any(Region.class));
        Assertions.assertNotNull(regionVO);
    }

    @Test
    void modify() {
        Region region = new Region();
        region.setCode(1101L);
        region.setName("广东省");
        region.setAlias("粤");
        region.setSuperior(1L);
        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(region);

        given(this.regionRepository.save(Mockito.any(Region.class))).willReturn(Mockito.mock(Region.class));

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setName("test");
        regionDTO.setPostalCode("23234");
        regionDTO.setDescription("描述");
        RegionVO regionVO = regionService.modify(11L, regionDTO);

        verify(this.regionRepository, times(1)).save(Mockito.any(Region.class));
        Assertions.assertNotNull(regionVO);
    }

    @Test
    void remove() {
        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mockito.mock(Region.class));

        regionService.remove(11L);

        verify(this.regionRepository, times(1)).deleteById(Mockito.anyLong());
    }
}