/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.repository.PermInfoDao;
import top.abeille.basic.authority.entity.PermInfo;
import top.abeille.basic.authority.service.PermInfoService;

import java.util.List;
import java.util.Optional;

/**
 * 权限资源信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class PermInfoServiceImpl implements PermInfoService {

    private final PermInfoDao permInfoDao;

    public PermInfoServiceImpl(PermInfoDao permInfoDao) {
        this.permInfoDao = permInfoDao;
    }

    @Override
    public Page<PermInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return permInfoDao.findAll(pageable);
    }

    @Override
    public List<PermInfo> findAll(Sort sort) {
        return permInfoDao.findAll(sort);
    }

    @Override
    public PermInfo save(PermInfo entity) {
        return permInfoDao.save(entity);
    }

    @Override
    public List<PermInfo> saveAll(List<PermInfo> entities) {
        return permInfoDao.saveAll(entities);
    }

    @Override
    public void removeById(Long id) {
        permInfoDao.deleteById(id);
    }

    @Override
    public void removeInBatch(List<PermInfo> entities) {
        permInfoDao.deleteInBatch(entities);
    }

    @Override
    public PermInfo getByExample(PermInfo permInfo) {
        Optional<PermInfo> optional = permInfoDao.findOne(Example.of(permInfo));
        return optional.orElse(null);
    }
}
