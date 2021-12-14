/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.entity.Role;
import io.leafage.basic.hypervisor.entity.User;
import io.leafage.basic.hypervisor.entity.UserRole;
import io.leafage.basic.hypervisor.repository.RoleRepository;
import io.leafage.basic.hypervisor.repository.UserRepository;
import io.leafage.basic.hypervisor.repository.UserRoleRepository;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;
import java.util.List;
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

    private static final String MESSAGE = "username is blank.";

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
    public Page<AccountVO> retrieve(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(sort) ? sort : "modifyTime"));
        return userRepository.findByEnabledTrue(pageable).map(this::convert);
    }

    @Override
    public UserVO create(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.save(user);
        return this.convertOuter(user);
    }

    @Override
    public UserVO modify(String username, UserDTO userDTO) {
        Assert.hasText(username, MESSAGE);
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        BeanUtils.copyProperties(userDTO, user);
        user = userRepository.saveAndFlush(user);
        return this.convertOuter(user);
    }

    @Override
    public void remove(String username) {
        Assert.hasText(username, MESSAGE);
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
    }

    @Override
    public UserVO fetch(String username) {
        Assert.hasText(username, MESSAGE);
        User user = userRepository.getByUsernameAndEnabledTrue(username);
        return this.convertOuter(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.getByUsernameOrPhoneOrEmailAndEnabledTrue(username, username, username);
        if (user == null) {
            return null;
        }
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        // 检查角色是否配置
        List<Role> roles = userRoles.stream().map(userRole ->
                        roleRepository.findById(userRole.getRoleId()).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toList());
        Set<GrantedAuthority> authorities = roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toSet());
        // 转换对象
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), authorities);
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private AccountVO convert(User user) {
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(user, accountVO);
        return accountVO;
    }

    /**
     * 转换为输出对象
     *
     * @return ExampleMatcher
     */
    private UserVO convertOuter(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
