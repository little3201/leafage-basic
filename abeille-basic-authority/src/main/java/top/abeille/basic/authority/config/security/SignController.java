/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */

package top.abeille.basic.authority.config.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证登录接口
 *
 * @author liwenqiang 2019-06-10 22:35
 **/
@RestController
public class SignController {

    @PostMapping("/sign")
    public ResponseEntity sign(String username, String password){
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return ResponseEntity.ok(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok("");
    }
}
