/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.dto.GroupDTO;
import top.abeille.basic.hypervisor.entity.Group;
import top.abeille.basic.hypervisor.repository.GroupRepository;
import top.abeille.basic.hypervisor.service.GroupService;
import top.abeille.basic.hypervisor.vo.GroupVO;
import top.abeille.common.basic.AbstractBasicService;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * 组织信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:25
 **/
@Service
public class GroupServiceImpl extends AbstractBasicService implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Page<GroupVO> retrieves(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> infoPage = groupRepository.findAll(pageable);
        if (CollectionUtils.isEmpty(infoPage.getContent())) {
            return new PageImpl<>(Collections.emptyList());
        }
        return infoPage.map(this::convertOuter);
    }

    @Override
    public GroupVO create(GroupDTO groupDTO) {
        Group info = new Group();
        BeanUtils.copyProperties(groupDTO, info);
        info.setCode(this.generateCode());
        info.setModifyTime(LocalDateTime.now());
        Group group = groupRepository.save(info);
        return this.convertOuter(group);
    }

    @Override
    public void remove(String code) {
        Group group = groupRepository.findByCodeAndEnabledTrue(code);
        groupRepository.deleteById(group.getId());
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private GroupVO convertOuter(Group info) {
        GroupVO groupVO = new GroupVO();
        BeanUtils.copyProperties(info, groupVO);
        return groupVO;
    }
}
