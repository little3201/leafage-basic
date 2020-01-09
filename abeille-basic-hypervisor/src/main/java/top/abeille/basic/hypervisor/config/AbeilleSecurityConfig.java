/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.security.UserDetailsServiceImpl;

/**
 * spring security 配置
 *
 * @author liwenqiang 2018/7/12 17:51
 **/
@EnableWebFluxSecurity
public class AbeilleSecurityConfig {

    private UserInfoRepository userInfoRepository;
    private RoleSourceRepository roleSourceRepository;
    private UserRoleRepository userRoleRepository;

    /**
     * 密码配置，使用BCryptPasswordEncoder
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义获取用户信息，此处使用mysql基于RBAC模式
     *
     * @return
     */
    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userInfoRepository, roleSourceRepository, userRoleRepository);
    }

    /**
     * 安全配置
     */
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated()
                .and().oauth2ResourceServer();
        return http.build();
    }
}
