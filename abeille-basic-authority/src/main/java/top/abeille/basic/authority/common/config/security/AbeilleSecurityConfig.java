package top.abeille.basic.authority.common.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * spring security 配置
 *
 * @author liwenqiang 2018/7/12 17:51
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AbeilleSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * http 请求安全配置
     *
     * @param http 安全请求
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and().authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and().cors()
                .and().csrf().disable();
    }

}
