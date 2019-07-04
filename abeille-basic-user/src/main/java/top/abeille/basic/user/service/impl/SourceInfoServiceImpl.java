/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.user.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.user.entity.SourceInfo;
import top.abeille.basic.user.repository.SourceInfoRepository;
import top.abeille.basic.user.service.SourceInfoService;

import java.util.List;
import java.util.Optional;

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
    public Page<SourceInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return sourceInfoRepository.findAll(pageable);
    }

    @Override
    public List<SourceInfo> findAll(Sort sort) {
        return sourceInfoRepository.findAll(sort);
    }

    @Override
    public SourceInfo save(SourceInfo entity) {
        return sourceInfoRepository.save(entity);
    }

    @Override
    public List<SourceInfo> saveAll(List<SourceInfo> entities) {
        return sourceInfoRepository.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        sourceInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<SourceInfo> entities) {
        sourceInfoRepository.deleteInBatch(entities);
    }

    @Override
    public SourceInfo getByExample(SourceInfo sourceInfo) {
        Optional<SourceInfo> optional = sourceInfoRepository.findOne(Example.of(sourceInfo));
        return optional.orElse(null);
    }
}
