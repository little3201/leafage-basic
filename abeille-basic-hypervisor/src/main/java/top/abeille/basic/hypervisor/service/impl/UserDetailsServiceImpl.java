/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.RoleInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.UserInfoService;

import java.util.*;

/**
 * 用户认证service实现
 *
 * @author liwenqiang 2018/10/18 21:18
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoService userInfoService;
    private final RoleInfoRepository roleInfoRepository;
    private final UserRoleRepository userRoleRepository;

    public UserDetailsServiceImpl(UserInfoService userInfoService, RoleInfoRepository roleInfoRepository, UserRoleRepository userRoleRepository) {
        this.userInfoService = userInfoService;
        this.roleInfoRepository = roleInfoRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 根据登录名查找用户信息
        UserInfo userInfo = userInfoService.loadUserByUsername(username);
        if (Objects.isNull(userInfo)) {
            throw new UsernameNotFoundException("username does not exist");
        }
        UserRole userRole = new UserRole();
        userRole.setEnabled(Boolean.TRUE);
        userRole.setUserId(userInfo.getUserId());
        List<UserRole> userRoles = userRoleRepository.findAll(Example.of(userRole));
        // 检查角色是否配置
        if (CollectionUtils.isEmpty(userRoles)) {
            throw new InsufficientAuthenticationException("permission denied");
        }
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        userRoles.stream().map(relation -> roleInfoRepository.findById(relation.getRoleId())).filter(Optional::isPresent)
                .map(Optional::get).forEach(roleInfo -> authorities.add(new SimpleGrantedAuthority(roleInfo.getRoleId())));
        return new User(userInfo.getUsername(), userInfo.getPassword(), userInfo.getEnabled(), userInfo.getAccountNonExpired(),
                userInfo.getCredentialsNonExpired(), userInfo.getAccountNonLocked(), authorities);
    }
}
