package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Region;
import io.leafage.basic.hypervisor.repository.RegionRepository;
import io.leafage.basic.hypervisor.service.RegionService;
import io.leafage.basic.hypervisor.vo.RegionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
    public RegionVO fetch(Long code) {
        Assert.notNull(code, "code is null.");
        Region region = regionRepository.getByCodeAndEnabledTrue(code);
        return this.convertOuter(region);
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
            Region superior = regionRepository.getByCodeAndEnabledTrue(region.getSuperior());
            vo.setSuperior(superior.getName());
        }
        return vo;
    }
}
