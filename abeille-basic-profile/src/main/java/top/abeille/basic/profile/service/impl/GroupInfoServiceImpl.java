/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import top.abeille.basic.profile.repository.GroupInfoRepository;
import top.abeille.basic.profile.entity.GroupInfo;
import top.abeille.basic.profile.service.GroupInfoService;

import java.util.List;
import java.util.Optional;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupInfoServiceImpl implements GroupInfoService {

    private final GroupInfoRepository groupInfoRepository;

    public GroupInfoServiceImpl(GroupInfoRepository groupInfoRepository) {
        this.groupInfoRepository = groupInfoRepository;
    }

    @Override
    public GroupInfo getById(Long id) {
        Optional<GroupInfo> optional = groupInfoRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Page<GroupInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        return groupInfoRepository.findAll(PageRequest.of(pageNum, pageSize));
    }

    @Override
    public GroupInfo save(GroupInfo entity) {
        return groupInfoRepository.save(entity);
    }

    @Override
    public void removeById(Long id) {
        groupInfoRepository.deleteById(id);
    }

    @Override
    public void removeInBatch(List<GroupInfo> entities) {
        groupInfoRepository.deleteInBatch(entities);
    }
}
