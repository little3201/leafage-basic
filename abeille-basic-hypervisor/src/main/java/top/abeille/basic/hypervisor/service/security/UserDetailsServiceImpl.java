/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.hypervisor.service.security;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.entity.UserRole;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * 用户认证service实现
 *
 * @author liwenqiang 2018/10/18 21:18
 **/
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    /**
     * email 正则
     */
    private static final String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 手机号 正则
     */
    private static final String REGEX_MOBILE = "0?(13|14|15|17|18|19)[0-9]{9}";

    private final UserInfoRepository userInfoRepository;
    private final RoleSourceRepository roleSourceRepository;
    private final UserRoleRepository userRoleRepository;

    public UserDetailsServiceImpl(UserInfoRepository userInfoRepository, RoleSourceRepository roleSourceRepository, UserRoleRepository userRoleRepository) {
        this.userInfoRepository = userInfoRepository;
        this.roleSourceRepository = roleSourceRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.loadUserByUsername(username).dematerialize();
    }

    /**
     * 设置必要参数匹配条件
     *
     * @param username 账号
     * @return ExampleMatcher
     */
    private Mono<User> loadUserByUsername(String username) {
        UserInfo info = new UserInfo();
        // 判断是邮箱还是手机号
        if (isMobile(username)) {
            info.setMobile(username);
        } else if (isEmail(username)) {
            info.setEmail(username);
        } else {
            throw new UsernameNotFoundException(username);
        }
        // 组装查询条件，只查询可用，未被锁定的用户信息
        ExampleMatcher exampleMatcher = appendConditions();
        return userInfoRepository.findOne(Example.of(info, exampleMatcher)).map(userVO -> {
            Set<GrantedAuthority> authorities = new LinkedHashSet<>();
            // 获取用户关联角色
            List<UserRole> userRoleList = userRoleRepository.findAllByUserIdAndEnabled(userVO.getId(), Boolean.TRUE);
            // 遍历获取资源ID
            userRoleList.forEach(userRole -> roleSourceRepository.findAllByRoleIdAndEnabled(userRole.getRoleId(), Boolean.TRUE)
                    .forEach(roleSource -> authorities.add(new SimpleGrantedAuthority(String.valueOf(roleSource.getSourceId())))));
            return new User(isMobile(username) ? userVO.getMobile() : userVO.getEmail(), userVO.getPassword(),
                    userVO.getEnabled(), userVO.getAccountNonExpired(), userVO.getCredentialsNonExpired(),
                    userVO.getAccountNonLocked(), authorities);
        });
    }

    /**
     * 设置必要参数匹配条件
     *
     * @return ExampleMatcher
     */
    private ExampleMatcher appendConditions() {
        String[] fields = new String[]{"is_enabled", "is_credentials_non_expired", "is_account_non_locked", "is_account_non_expired"};
        ExampleMatcher matcher = ExampleMatcher.matching();
        for (String param : fields) {
            matcher.withMatcher(param, exact());
        }
        return matcher;
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
}
