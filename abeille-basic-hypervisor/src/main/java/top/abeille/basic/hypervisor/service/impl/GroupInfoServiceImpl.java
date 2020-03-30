/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.constant.PrefixEnum;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.entity.GroupInfo;
import top.abeille.basic.hypervisor.repository.GroupInfoRepository;
import top.abeille.basic.hypervisor.service.GroupInfoService;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupInfoServiceImpl extends AbstractBasicService implements GroupInfoService {

    private final GroupInfoRepository groupInfoRepository;

    public GroupInfoServiceImpl(GroupInfoRepository groupInfoRepository) {
        this.groupInfoRepository = groupInfoRepository;
    }

    @Override
    public Page<GroupVO> retrieveByPage(Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("is_enabled", exact());
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setEnabled(true);
        Page<GroupInfo> infoPage = groupInfoRepository.findAll(Example.of(groupInfo, matcher), pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public GroupVO create(GroupDTO groupDTO) {
        GroupInfo info = new GroupInfo();
        BeanUtils.copyProperties(groupDTO, info);
        info.setBusinessId(PrefixEnum.GP + this.generateId());
        info.setModifyTime(LocalDateTime.now());
        GroupInfo groupInfo = groupInfoRepository.save(info);
        return this.convertOuter(groupInfo);
    }

    @Override
    public void removeById(String businessId) {
        Optional<GroupInfo> optional = this.fetchInfo(businessId);
        if (optional.isPresent()) {
            GroupInfo info = optional.get();
            groupInfoRepository.deleteById(info.getId());
        }
    }

    @Override
    public void removeInBatch(List<GroupDTO> entities) {
    }

    /**
     * 根据业务ID查信息
     *
     * @param businessId 业务ID
     * @return 数据库对象信息
     */
    private Optional<GroupInfo> fetchInfo(String businessId) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setBusinessId(businessId);
        return groupInfoRepository.findOne(Example.of(groupInfo));
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private GroupVO convertOuter(GroupInfo info) {
        GroupVO groupVO = new GroupVO();
        BeanUtils.copyProperties(info, groupVO);
        return groupVO;
    }
}
