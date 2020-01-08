/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户认证service实现
 *
 * @author liwenqiang 2018/10/18 21:18
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoService userInfoService;

    public UserDetailsServiceImpl(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 根据登录名查找用户信息
        UserDetailsVO user = userInfoService.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username does not exist");
        }
        // 检查角色是否配置
        if (CollectionUtils.isEmpty(user.getAuthorities())) {
            throw new InsufficientAuthenticationException("permission denied");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<String> authoritiesSet = user.getAuthorities();
        authoritiesSet.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(), authorities);
    }
}
