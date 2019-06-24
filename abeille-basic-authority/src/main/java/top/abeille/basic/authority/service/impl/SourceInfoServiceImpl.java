/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.entity.SourceInfo;
import top.abeille.basic.authority.repository.SourceInfoDao;
import top.abeille.basic.authority.service.SourceInfoService;

import java.util.List;
import java.util.Optional;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class SourceInfoServiceImpl implements SourceInfoService {

    private final SourceInfoDao sourceInfoDao;

    public SourceInfoServiceImpl(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public Page<SourceInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return sourceInfoDao.findAll(pageable);
    }

    @Override
    public List<SourceInfo> findAll(Sort sort) {
        return sourceInfoDao.findAll(sort);
    }

    @Override
    public SourceInfo save(SourceInfo entity) {
        return sourceInfoDao.save(entity);
    }

    @Override
    public List<SourceInfo> saveAll(List<SourceInfo> entities) {
        return sourceInfoDao.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        sourceInfoDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<SourceInfo> entities) {
        sourceInfoDao.deleteInBatch(entities);
    }

    @Override
    public SourceInfo getByExample(SourceInfo sourceInfo) {
        Optional<SourceInfo> optional = sourceInfoDao.findOne(Example.of(sourceInfo));
        return optional.orElse(null);
    }
}
