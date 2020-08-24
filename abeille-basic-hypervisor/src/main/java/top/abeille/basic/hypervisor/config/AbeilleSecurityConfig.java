/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import top.abeille.basic.hypervisor.handler.AbeilleFailureHandler;
import top.abeille.basic.hypervisor.handler.AbeilleSuccessHandler;
import top.abeille.basic.hypervisor.repository.RoleSourceRepository;
import top.abeille.basic.hypervisor.repository.SourceRepository;
import top.abeille.basic.hypervisor.repository.UserRepository;
import top.abeille.basic.hypervisor.repository.UserRoleRepository;
import top.abeille.basic.hypervisor.service.security.UserDetailsServiceImpl;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * spring security 配置
 *
 * @author liwenqiang 2019/7/12 17:51
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class AbeilleSecurityConfig {

    private static final String KEY_STORE = "jwt/abeille-top-jwt.jks";
    private static final String KEY_PASS = "abeille-top";
    private static final String ALIAS = "abeille-top-jwt";

    private final UserRepository userRepository;
    private final RoleSourceRepository roleSourceRepository;
    private final UserRoleRepository userRoleRepository;
    private final SourceRepository sourceRepository;

    public AbeilleSecurityConfig(UserRepository userRepository, RoleSourceRepository roleSourceRepository,
                                 UserRoleRepository userRoleRepository, SourceRepository sourceRepository) {
        this.userRepository = userRepository;
        this.roleSourceRepository = roleSourceRepository;
        this.userRoleRepository = userRoleRepository;
        this.sourceRepository = sourceRepository;
    }

    /**
     * 密码配置，使用BCryptPasswordEncoder
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository, roleSourceRepository, userRoleRepository, sourceRepository);
    }

    /**
     * 安全配置
     */
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange().pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(HttpMethod.GET, "/user/{segment}").permitAll()
                .anyExchange().authenticated()
                .and().exceptionHandling()
                .and().formLogin().authenticationSuccessHandler(authenticationSuccessHandler())
                .authenticationFailureHandler(authenticationFailureHandler())
                .and().logout()
                .and().csrf().disable()
//                .and().csrf().csrfTokenRepository(new CookieServerCsrfTokenRepository())
                .oauth2ResourceServer().jwt().jwtDecoder(jwtDecoder());
        return http.build();
    }

    /**
     * 登陆成功后执行的处理器
     */
    private ServerAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AbeilleSuccessHandler();
    }

    /**
     * 登陆失败后执行的处理器
     */
    private ServerAuthenticationFailureHandler authenticationFailureHandler() {
        return new AbeilleFailureHandler();
    }

    /**
     * JWT 解码器
     */
    private ReactiveJwtDecoder jwtDecoder() {
        return new NimbusReactiveJwtDecoder(readRSAPublicKey());
    }

    /**
     * 获取RSAPublicKey
     */
    private RSAPublicKey readRSAPublicKey() {
        ClassPathResource ksFile = new ClassPathResource(KEY_STORE);
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, KEY_PASS.toCharArray());
        KeyPair keyPair = ksFactory.getKeyPair(ALIAS);
        return (RSAPublicKey) keyPair.getPublic();
    }
}
