package io.leafage.basic.hypervisor.service.impl;

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
import org.springframework.data.domain.PageRequest;
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
        given(this.regionRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(Flux.just(Mockito.mock(Region.class)));

        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Region.class)));

        StepVerifier.create(regionService.retrieve(0, 2)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        Region region = new Region();
        region.setId(new ObjectId());
        region.setCode(2L);
        region.setName("test");
        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(region));

        StepVerifier.create(regionService.fetch(1100L)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.regionRepository.insert(Mockito.any(Region.class))).willReturn(Mono.just(Mockito.mock(Region.class)));

        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Region.class)));

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setCode(11001L);
        regionDTO.setName("测试村");
        StepVerifier.create(regionService.create(regionDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create_superior() {
        given(this.regionRepository.insert(Mockito.any(Region.class))).willReturn(Mono.just(Mockito.mock(Region.class)));

        given(this.regionRepository.getByCodeAndEnabledTrue(Mockito.anyLong())).willReturn(Mono.just(Mockito.mock(Region.class)));

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setCode(11001L);
        regionDTO.setName("测试村");
        regionDTO.setSuperior(1100L);
        StepVerifier.create(regionService.create(regionDTO)).expectNextCount(1).verifyComplete();
    }

    @Test
    void count() {
        given(this.regionRepository.count()).willReturn(Mono.just(2L));

        StepVerifier.create(regionService.count()).expectNextCount(1).verifyComplete();
    }
}