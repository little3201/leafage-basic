/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.endpoint;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * spring security 配置
 *
 * @author liwenqiang 2019/11/12 17:51
 **/
@FrameworkEndpoint
public class AbeilleTokenEndpoint {

//    private final TokenStore tokenStore;
//
//    public AbeilleTokenEndpoint(TokenStore tokenStore) {
//        this.tokenStore = tokenStore;
//    }

    /**
     * 退出并删除token
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtils.isBlank(authHeader)) {
            return ResponseEntity.badRequest().build();
        }

        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StringUtils.EMPTY).trim();
//        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
//        if (accessToken == null || StringUtils.isBlank(accessToken.getValue())) {
//            return ResponseEntity.badRequest().build();
//        }
//        tokenStore.removeAccessToken(accessToken);
//
//        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
//        tokenStore.removeRefreshToken(refreshToken);
        return ResponseEntity.ok("退出成功");
    }

}
