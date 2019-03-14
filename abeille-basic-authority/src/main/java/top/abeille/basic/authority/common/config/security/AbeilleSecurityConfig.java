package top.abeille.basic.authority.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.abeille.basic.authority.common.config.security.handler.LoginFailureHandler;
import top.abeille.basic.authority.common.config.security.handler.LoginSuccessHandler;
import top.abeille.basic.authority.common.config.security.handler.LogoutSuccessHandler;

/**
 * spring security 配置
 *
 * @author liwenqiang 2018/7/12 17:51
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AbeilleSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    public AbeilleSecurityConfig(LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler,
                                 LogoutSuccessHandler logoutSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

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
        http.formLogin()
                /* loginProcessingUrl 配置最好写在 loginPage 之前 */
                .loginProcessingUrl("/authentication").loginPage("/login")
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and().logout().deleteCookies("remove").invalidateHttpSession(false).logoutSuccessHandler(logoutSuccessHandler)
                .and().authorizeRequests().antMatchers("/login", "/login.html").permitAll()
                /*swagger 访问许可配置*/
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs",
                        "/webjars/springfox-swagger-ui/**").permitAll()
                .anyRequest().authenticated()
                .and().cors()
                /* 下面的配置必须加，否则，会一直在登录页面循环，无法通过认证 */
                .and().csrf().disable();
    }

    /**
     * 静态资源许可
     *
     * @param web 资源配置
     * @throws Exception 异常
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().mvcMatchers("/**/css/*.css", "/**/images/*.png");
    }

}
