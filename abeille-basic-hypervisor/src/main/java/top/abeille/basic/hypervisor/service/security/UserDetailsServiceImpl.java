/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.hypervisor.service.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.repository.SourceRepository;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 用户认证service实现
 *
 * @author liwenqiang 2018/10/18 21:18
 **/
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    /**
     * 开启日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    /**
     * email 正则
     */
    private static final String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    /**
     * 手机号 正则
     */
    private static final String REGEX_MOBILE = "0?(13|14|15|17|18|19)[0-9]{9}";

    private final UserRepository userRepository;
    private final RoleSourceRepository roleSourceRepository;
    private final UserRoleRepository userRoleRepository;
    private final SourceRepository sourceRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, RoleSourceRepository roleSourceRepository,
                                  UserRoleRepository userRoleRepository, SourceRepository sourceRepository) {
        this.userRepository = userRepository;
        this.roleSourceRepository = roleSourceRepository;
        this.userRoleRepository = userRoleRepository;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        UserInfo info = new UserInfo();
        // 判断是邮箱还是手机号
        if (isMobile(username)) {
            info.setMobile(username);
        } else if (isEmail(username)) {
            info.setEmail(username);
        } else {
            logger.error("{} 用户信息不存在", username);
            throw new UsernameNotFoundException(username);
        }
        return this.loadUser(info);
    }

    /**
     * 请求用户信息，并转载为UserDetails
     *
     * @param info 请求对象
     * @return ExampleMatcher
     */
    private Mono<UserDetails> loadUser(UserInfo info) {
        return userRepository.findOne(Example.of(info)).map(userInfo -> {
            // 获取关联的角色，然后获取用户信息
            Set<GrantedAuthority> authorities = new LinkedHashSet<>();
            // 根据用户主键ID查询关联的资源ID
            userRoleRepository.findAllByUserIdAndEnabled(userInfo.getId(), true).subscribe(userRole ->
                    roleSourceRepository.findAllByRoleIdAndEnabled(userRole.getRoleId(), true).subscribe(roleSource ->
                            sourceRepository.findById(roleSource.getSourceId()).subscribe(sourceInfo ->
                                    authorities.add(new SimpleGrantedAuthority(sourceInfo.getBusinessId()))
                            )
                    )
            );
            // 返回user
            return new User(StringUtils.isNotBlank(info.getMobile()) ? info.getMobile() : info.getEmail(), userInfo.getPassword(),
                    userInfo.getEnabled(), userInfo.getAccountNonExpired(), userInfo.getCredentialsNonExpired(),
                    userInfo.getAccountNonLocked(), authorities);
        });
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
