/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * spring security 配置
 *
 * @author liwenqiang 2019/7/12 17:51
 **/
@EnableWebFluxSecurity
public class AbeilleSecurityConfig {

    private static final String CERT_PATH = "jwt/public.cert";
    private static final String CERT_TYPE = "RSA";

    /**
     * 密码配置，使用BCryptPasswordEncoder
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全配置
     */
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.csrf().disable().formLogin().disable().httpBasic().disable()
                .authorizeExchange().pathMatchers(HttpMethod.OPTIONS).permitAll().anyExchange().authenticated()
                .and().exceptionHandling()
                .and().oauth2ResourceServer().jwt().jwtDecoder(jwtDecoder());
        return http.build();
    }

    /**
     * JWT 解码器
     */
    private ReactiveJwtDecoder jwtDecoder() throws Exception {
        return new NimbusReactiveJwtDecoder(readRSAPublicKey());
    }

    /**
     * 获取RSAPublicKey
     */
    private RSAPublicKey readRSAPublicKey() throws Exception {
        // 从资源目录下的文件中读取证书文件，获取Public Key
        InputStream is = new ClassPathResource(CERT_PATH).getInputStream();
        KeySpec keySpec = new X509EncodedKeySpec(new byte[is.available()]);
        KeyFactory keyFactory = KeyFactory.getInstance(CERT_TYPE);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
