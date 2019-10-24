/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.stereotype.Service;
import top.abeille.basic.hypervisor.dto.SourceDTO;
import top.abeille.basic.hypervisor.repository.SourceInfoRepository;
import top.abeille.basic.hypervisor.service.SourceInfoService;
import top.abeille.basic.hypervisor.vo.SourceVO;

import java.util.List;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class SourceInfoServiceImpl implements SourceInfoService {

    private final SourceInfoRepository sourceInfoRepository;

    public SourceInfoServiceImpl(SourceInfoRepository sourceInfoRepository) {
        this.sourceInfoRepository = sourceInfoRepository;
    }

    @Override
    public List<SourceVO> saveAll(List<SourceDTO> entities) {
        return null;
    }

    @Override
    public void removeById(Long id) {
        sourceInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<SourceDTO> entities) {
    }
}
