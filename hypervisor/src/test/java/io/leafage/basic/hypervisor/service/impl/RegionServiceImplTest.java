package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Region;
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
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;

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
        superior.setId(region.getSuperior());
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
}