/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.payment.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.abeille.basic.payment.entity.GroupInfo;
import top.abeille.basic.payment.repository.GroupInfoRepository;
import top.abeille.basic.payment.service.GroupInfoService;

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

    private final RedisTemplate<String, Page<GroupInfo>> redisTemplate;

    public GroupInfoServiceImpl(GroupInfoRepository groupInfoRepository, RedisTemplate<String, Page<GroupInfo>> redisTemplate) {
        this.groupInfoRepository = groupInfoRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GroupInfo getById(Long id) {
        Optional<GroupInfo> optional = groupInfoRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Page<GroupInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Boolean hasKey = redisTemplate.hasKey("groups");
        Page<GroupInfo> groups;
        if (null != hasKey && hasKey) {
            groups = redisTemplate.opsForValue().get("groups");
            if (null != groups) {
                return groups;
            }
        }
        groups = groupInfoRepository.findAll(PageRequest.of(pageNum, pageSize));
        redisTemplate.opsForValue().set("groups", groups);
        return groups;
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
