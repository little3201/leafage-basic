package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.document.Region;
import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.NoSuchElementException;

/**
 * region service impl
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
@Service
public class RegionServiceImpl implements RegionService {

    private static final String CODE_MESSAGE = "code must not null";

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Flux<RegionVO> retrieve(int page, int size) {
        return regionRepository.findByEnabledTrue(PageRequest.of(page, size)).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RegionVO> fetch(Long code) {
        Assert.notNull(code, CODE_MESSAGE);
        return regionRepository.getByCodeAndEnabledTrue(code).flatMap(this::fetchOuter);
    }

    @Override
    public Mono<Boolean> exist(String name) {
        return regionRepository.existsByName(name);
    }

    @Override
    public Mono<Long> count() {
        return regionRepository.count();
    }

    @Override
    public Flux<RegionVO> lower(long code) {
        return regionRepository.findByCodeBetweenAndEnabledTrue(code * 100, code * 100 + 99).flatMap(this::fetchOuter);
    }

    @Override
    public Mono<RegionVO> create(RegionDTO regionDTO) {
        return Mono.just(regionDTO).map(dto -> {
                    Region region = new Region();
                    BeanUtils.copyProperties(regionDTO, region);
                    return region;
                })
                .flatMap(regionRepository::insert).flatMap(this::convertOuter);
    }

    @Override
    public Mono<RegionVO> modify(Long code, RegionDTO regionDTO) {
        Assert.notNull(code, CODE_MESSAGE);
        return regionRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(region -> BeanUtils.copyProperties(regionDTO, region))
                .flatMap(regionRepository::save).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Void> remove(Long code) {
        Assert.notNull(code, CODE_MESSAGE);
        return regionRepository.getByCodeAndEnabledTrue(code).flatMap(group ->
                regionRepository.deleteById(group.getId()));
    }

    /**
     * 数据转换
     *
     * @param region 信息
     * @return RegionVO 输出对象
     */
    private Mono<RegionVO> fetchOuter(Region region) {
        return Mono.just(region).map(r -> {
            RegionVO vo = new RegionVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).flatMap(vo -> {
            if (region.getSuperior() != null) {
                return regionRepository.getByCodeAndEnabledTrue(region.getSuperior()).map(superior -> {
                    vo.setSuperior(String.valueOf(superior.getCode()));
                    return vo;
                }).switchIfEmpty(Mono.just(vo));
            }
            return Mono.just(vo);
        });
    }

    /**
     * 数据转换
     *
     * @param region 信息
     * @return RegionVO 输出对象
     */
    private Mono<RegionVO> convertOuter(Region region) {
        Mono<RegionVO> voMono = Mono.just(region).map(r -> {
            RegionVO vo = new RegionVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        });

        if (region.getSuperior() != null) {
            Mono<Region> superiorMono = regionRepository.getByCodeAndEnabledTrue(region.getSuperior());
            return voMono.zipWith(superiorMono, (vo, superior) -> {
                vo.setSuperior(superior.getName());
                return vo;
            });
        }
        return voMono;
    }
}
