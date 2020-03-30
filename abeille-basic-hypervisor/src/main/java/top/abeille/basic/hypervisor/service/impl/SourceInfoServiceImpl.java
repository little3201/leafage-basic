/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.entity.SourceInfo;
import top.abeille.basic.hypervisor.repository.SourceInfoRepository;
import top.abeille.basic.hypervisor.service.SourceInfoService;
import top.abeille.basic.hypervisor.vo.SourceVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class SourceInfoServiceImpl extends AbstractBasicService implements SourceInfoService {

    private final SourceInfoRepository sourceInfoRepository;

    public SourceInfoServiceImpl(SourceInfoRepository sourceInfoRepository) {
        this.sourceInfoRepository = sourceInfoRepository;
    }

    @Override
    public Page<SourceVO> retrieveByPage(Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("is_enabled", exact());
        SourceInfo roleInfo = new SourceInfo();
        roleInfo.setEnabled(true);
        Page<SourceInfo> infoPage = sourceInfoRepository.findAll(Example.of(roleInfo, matcher), pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public SourceVO create(SourceDTO sourceDTO) {
        SourceInfo info = new SourceInfo();
        BeanUtils.copyProperties(sourceDTO, info);
        info.setBusinessId(PrefixEnum.SR + this.generateId());
        info.setModifyTime(LocalDateTime.now());
        SourceInfo sourceInfo = sourceInfoRepository.save(info);
        return this.convertOuter(sourceInfo);
    }

    @Override
    public List<SourceVO> saveAll(List<SourceDTO> entities) {
        return null;
    }

    @Override
    public void removeById(String businessId) {
        Optional<SourceInfo> optional = this.fetchInfo(businessId);
        if (optional.isPresent()) {
            SourceInfo info = optional.get();
            sourceInfoRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeInBatch(List<SourceDTO> entities) {
    }

    /**
     * 根据业务ID查信息
     *
     * @param businessId 业务ID
     * @return 数据库对象信息
     */
    private Optional<SourceInfo> fetchInfo(String businessId) {
        SourceInfo sourceInfo = new SourceInfo();
        sourceInfo.setBusinessId(businessId);
        return sourceInfoRepository.findOne(Example.of(sourceInfo));
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private SourceVO convertOuter(SourceInfo info) {
        SourceVO sourceVO = new SourceVO();
        BeanUtils.copyProperties(info, sourceVO);
        return sourceVO;
    }
}
