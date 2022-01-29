package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.RegionDTO;
import io.leafage.basic.hypervisor.entity.Region;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * region service impl.
 *
 * @author liwenqiang 2021/11/27 14:18
 **/
@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Page<RegionVO> retrieve(int page, int size) {
        return regionRepository.findByEnabledTrue(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public List<RegionVO> child(long code) {
        List<Region> regions = regionRepository.findByCodeBetweenAndEnabledTrue(code * 100, code * 100 + 99);
        return regions.stream().map(this::convertOuter).collect(Collectors.toList());
    }

    @Override
    public RegionVO fetch(Long code) {
        Assert.notNull(code, "code must not null.");
        Region region = regionRepository.getByCodeAndEnabledTrue(code);
        return this.convertOuter(region);
    }

    @Override
    public boolean exist(String name) {
        Assert.hasText(name, "name must not null.");
        return regionRepository.existsByName(name);
    }

    @Override
    public RegionVO create(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        regionRepository.save(region);
        return this.convertOuter(region);
    }

    @Override
    public RegionVO modify(Long code, RegionDTO regionDTO) {
        Region region = regionRepository.getByCodeAndEnabledTrue(code);
        BeanUtils.copyProperties(regionDTO, region);
        regionRepository.saveAndFlush(region);
        return this.convertOuter(region);
    }

    @Override
    public void remove(Long code) {
        Region region = regionRepository.getByCodeAndEnabledTrue(code);
        regionRepository.deleteById(region.getId());
    }

    /**
     * 数据转换
     *
     * @param region 信息
     * @return RegionVO 输出对象
     */
    private RegionVO convertOuter(Region region) {
        RegionVO vo = new RegionVO();
        BeanUtils.copyProperties(region, vo);

        if (region.getSuperior() != null) {
            Optional<Region> optional = regionRepository.findById(region.getSuperior());
            optional.ifPresent(superior -> vo.setSuperior(superior.getName()));
        }
        return vo;
    }
}
