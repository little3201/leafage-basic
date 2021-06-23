/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息service实现.
 *
 * @author liwenqiang 2018/7/28 0:30
 **/
@Service
public class UserServiceImpl extends AbstractBasicService implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<UserVO> retrieve(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(order) ? order : "modifyTime"));
        return userRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.save(user);
        if (!CollectionUtils.isEmpty(userDTO.getRoles())) {
            List<Role> roles = roleRepository.findByCodeInAndEnabledTrue(userDTO.getRoles());
            if (!CollectionUtils.isEmpty(roles)) {
                this.saveBatch(roles, user.getId());
            }
        }
        return this.convertOuter(user);
    }

    @Override
    public UserVO modify(String username, UserDTO userDTO) {
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.saveAndFlush(user);
        if (CollectionUtils.isEmpty(userDTO.getRoles())) {
            userRoleRepository.deleteByUserId(user.getId());
        } else {
            List<Role> roles = roleRepository.findByCodeInAndEnabledTrue(userDTO.getRoles());
            // 查已存在的
            List<UserRole> userRoleList = userRoleRepository.findByUserId(user.getId());
            // 删除去掉的
            List<UserRole> userRoles = this.notExisted(userRoleList, roles);
            userRoles.forEach(userRole -> userRole.setEnabled(false));
            userRoleRepository.saveAll(userRoles);
            // 保存新增的
            List<Role> newRoles = this.addNew(userRoleList, roles);
            this.saveBatch(newRoles, user.getId());
        }
        return this.convertOuter(user);
    }

    @Override
    public void remove(String username) {
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
    }

    @Override
    public UserVO fetch(String username) {
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        return this.convertOuter(user);
    }

    @Override
    public UserDetails fetchDetails(String username) {
        User user = userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username);
        if (user == null) {
            return null;
        }
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        // 检查角色是否配置
        List<Role> roles = userRoles.stream().map(userRole ->
                roleRepository.findById(userRole.getRoleId()).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toList());
        Set<String> authorities = roles.stream().map(Role::getCode).collect(Collectors.toSet());
        // 转换对象
        UserDetails userDetails = new UserDetails();
        BeanUtils.copyProperties(user, userDetails);
        userDetails.setAuthorities(authorities);
        return userDetails;
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private UserVO convertOuter(User info) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(info, userVO);
        return userVO;
    }

    /**
     * 批量执行
     *
     * @param roles  角色
     * @param userId 用户ID
     */
    private void saveBatch(List<Role> roles, long userId) {
        List<UserRole> userRoles = roles
                .stream().map(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(role.getId());
                    userRole.setUserId(userId);
                    userRole.setModifier(role.getModifier());
                    return userRole;
                }).collect(Collectors.toList());
        userRoleRepository.saveAll(userRoles);
    }

    /**
     * 新增信息
     *
     * @param userRoles 已存在
     * @param roles     新提交
     * @return 新增信息
     */
    private List<Role> addNew(List<UserRole> userRoles, List<Role> roles) {
        Map<Long, Long> map = userRoles.stream().collect(Collectors.toMap(UserRole::getRoleId, UserRole::getId));
        // 保存新增的
        List<Role> existed = roles.stream().filter(role -> map.containsKey(role.getId())).collect(Collectors.toList());
        roles.removeAll(existed);
        return roles;
    }

    /**
     * 移除信息
     *
     * @param userRoles 已存在
     * @param roles     新提交
     * @return 移除信息
     */
    private List<UserRole> notExisted(List<UserRole> userRoles, List<Role> roles) {
        Map<Long, String> map = roles.stream().collect(Collectors.toMap(Role::getId, Role::getCode));
        // 仍旧存在的
        List<UserRole> existed = userRoles.stream().filter(userRole -> map.containsKey(userRole.getRoleId())).collect(Collectors.toList());
        userRoles.removeAll(existed);
        // 不在存在的
        return userRoles;
    }
}
