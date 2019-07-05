/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.config.oauth2;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * oauth2 resource server common
 *
 * @author liwenqiang 2018/12/31 14:42
 **/
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * Http 配置
     *
     * @param http 安全信息
     * @throws Exception 异常
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new Oauth2RequestedMatcher())
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and().cors();
    }

    /**
     * 定义OAuth2请求匹配器
     */
    private static class Oauth2RequestedMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            // 判断来源请求是否包含oauth2授权信息,这里授权信息来源可能是头部的Authorization值以Bearer开头,
            String auth = request.getHeader("Authorization");
            boolean haveOauth2Token = StringUtils.isNotBlank(auth) && auth.startsWith("Bearer");
            // 或者是请求参数中包含access_token参数,满足其中一个则匹配成功
            String accessToken = request.getParameter("access_token");
            boolean haveAccessToken = StringUtils.isNotBlank(accessToken);
            return haveOauth2Token || haveAccessToken;
        }
    }


}
