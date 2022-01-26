package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.entity.Group;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.entity.UserGroup;
import io.leafage.basic.hypervisor.repository.GroupRepository;
import io.leafage.basic.hypervisor.repository.UserGroupRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.service.UserGroupService;
import io.leafage.basic.hypervisor.vo.GroupVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;

    public UserGroupServiceImpl(UserRepository userRepository, UserGroupRepository userGroupRepository,
                                GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<UserVO> users(String code) {
        Group group = groupRepository.getByCodeAndEnabledTrue(code);
        if (group == null) {
            return Collections.emptyList();
        }
        List<UserGroup> userGroups = userGroupRepository.findByGroupId(group.getId());
        return userGroups.stream().map(userGroup -> userRepository.findById(userGroup.getUserId()))
                .map(Optional::orElseThrow).map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return userVO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<GroupVO> groups(String username) {
        Assert.hasText(username, "username is blank");
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        if (user == null) {
            return Collections.emptyList();
        }
        List<UserGroup> userGroups = userGroupRepository.findByUserId(user.getId());
        if (CollectionUtils.isEmpty(userGroups)) {
            return Collections.emptyList();
        }
        return userGroups.stream().map(userGroup -> groupRepository.findById(userGroup.getGroupId()))
                .map(Optional::orElseThrow).map(role -> {
                    GroupVO groupVO = new GroupVO();
                    BeanUtils.copyProperties(role, groupVO);
                    return groupVO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<UserGroup> relation(String username, Set<String> groups) {
        Assert.hasText(username, "username is blank");
        Assert.notNull(groups, "groups is null");
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        if (user == null) {
            return Collections.emptyList();
        }
        List<UserGroup> userGroups = groups.stream().map(s -> {
            Group group = groupRepository.getByCodeAndEnabledTrue(s);
            UserGroup userRole = new UserGroup();
            userRole.setUserId(user.getId());
            userRole.setGroupId(group.getId());
            return userRole;
        }).collect(Collectors.toList());
        return userGroupRepository.saveAll(userGroups);
    }
}
