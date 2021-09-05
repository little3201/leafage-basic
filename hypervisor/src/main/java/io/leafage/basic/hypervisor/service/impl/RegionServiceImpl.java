package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Region;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Flux<RegionVO> retrieve(int page, int size) {
        return regionRepository.findByEnabledTrue(PageRequest.of(page, size)).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RegionVO> fetch(long code) {
        return regionRepository.getByCodeAndEnabledTrue(code).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RegionVO> create(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        return regionRepository.insert(region).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Long> count() {
        return regionRepository.count();
    }

    /**
     * 数据转换
     *
     * @param info 信息
     * @return RegionVO 输出对象
     */
    private Mono<RegionVO> convertOuter(Region info) {
        Mono<RegionVO> voMono = Mono.just(info).map(region -> {
            RegionVO outer = new RegionVO();
            BeanUtils.copyProperties(region, outer);
            return outer;
        });

        if (info.getSuperior() != null) {
            Mono<Region> superiorMono = regionRepository.getByCodeAndEnabledTrue(info.getSuperior());
            return voMono.zipWith(superiorMono, (vo, superior) -> {
                vo.setSuperior(superior.getName());
                return vo;
            });
        }
        return voMono;
    }
}
