/*
 * Copyright (c) 2025.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wq li
 * @since 1.4
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${app.base-uri}")
    private String appBaseUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();

        http
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated()
                )
                .csrf(csrf ->
                        csrf.csrfTokenRepository(cookieCsrfTokenRepository)
                )
                .cors(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint())
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login.successHandler(new SimpleUrlAuthenticationSuccessHandler(appBaseUri)))
                .logout(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        AuthenticationEntryPoint authenticationEntryPoint =
                new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/web-client");

        MediaTypeRequestMatcher textHtmlMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        textHtmlMatcher.setUseEquals(true);

        List<RequestMatcherEntry<AuthenticationEntryPoint>> entryPoints = new ArrayList<>();
        entryPoints.add(new RequestMatcherEntry<>(textHtmlMatcher, authenticationEntryPoint));

        return new DelegatingAuthenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), entryPoints);
    }
}
