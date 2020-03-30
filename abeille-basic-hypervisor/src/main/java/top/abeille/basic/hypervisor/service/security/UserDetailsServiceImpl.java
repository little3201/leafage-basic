/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 用户认证service实现
 *
 * @author liwenqiang 2018/10/18 21:18
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 开启日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    /**
     * email 正则
     */
    private static final String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    private final RoleInfoRepository roleInfoRepository;
    private final UserRoleRepository userRoleRepository;
    /**
     * 手机号 正则
     */
    private static final String REGEX_MOBILE = "0?(13|14|15|17|18|19)[0-9]{9}";
    private final UserInfoRepository userInfoRepository;

    public UserDetailsServiceImpl(UserInfoRepository userInfoRepository, RoleInfoRepository roleInfoRepository, UserRoleRepository userRoleRepository) {
        this.userInfoRepository = userInfoRepository;
        this.roleInfoRepository = roleInfoRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 判断是邮箱还是手机号
        UserInfo userInfo = new UserInfo();
        if (isMobile(username)) {
            userInfo.setMobile(username);
        } else if (isEmail(username)) {
            userInfo.setEmail(username);
        } else {
            logger.error("{} 用户信息不存在", username);
            throw new UsernameNotFoundException(username);
        }
        // 根据登录名查找用户信息
        appendParams(userInfo);
        Optional<UserInfo> infoOptional = userInfoRepository.findOne(Example.of(userInfo));
        if (infoOptional.isEmpty()) {
            throw new UsernameNotFoundException("username does not exist");
        }
        UserRole ur = new UserRole();
        ur.setEnabled(Boolean.TRUE);
        ur.setUserId(userInfo.getId());
        List<UserRole> userRoles = userRoleRepository.findAll(Example.of(ur));
        // 检查角色是否配置
        if (CollectionUtils.isEmpty(userRoles)) {
            throw new InsufficientAuthenticationException("permission denied");
        }
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        userRoles.stream().map(userRole -> roleInfoRepository.findById(userRole.getRoleId())).filter(Optional::isPresent)
                .map(Optional::get).forEach(roleInfo -> authorities.add(new SimpleGrantedAuthority(roleInfo.getBusinessId())));
        return new User(isMobile(username) ? userInfo.getMobile() : userInfo.getEmail(), userInfo.getPassword(), userInfo.getEnabled(), userInfo.getAccountNonExpired(),
                userInfo.getCredentialsNonExpired(), userInfo.getAccountNonLocked(), authorities);
    }

    /**
     * 是否手机号
     *
     * @param mobile 匹配字符
     * @return true if mather otherwise false
     */
    private boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 是否邮箱
     *
     * @param email 匹配字符
     * @return true if mather otherwise false
     */
    private boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param userInfo 用户信息
     * @return UserInfo
     */
    private void appendParams(UserInfo userInfo) {
        userInfo.setEnabled(true);
        userInfo.setAccountNonExpired(true);
        userInfo.setCredentialsNonExpired(true);
        userInfo.setAccountNonLocked(true);
    }
}
