/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.entity.UserInfo;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

/**
 * 用户信息Controller
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@RestController
@RequestMapping("/user")
public class UserInfoController extends AbstractController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 用户查询——分页
     *
     * @return ResponseEntity
     */
    @GetMapping
    public Flux<UserInfo> fetchUsers() {
        return userInfoService.findAll();
    }

    /**
     * 用户查询——根据ID
     *
     * @param userId 用户ID
     * @return ResponseEntity
     */
    @GetMapping("/{userId}")
    public Mono<UserInfo> getUser(@PathVariable String userId) {
        return userInfoService.getByUserId(userId);
    }

    /**
     * 用户查询——根据用户名
     *
     * @param username 用户名
     * @return ResponseEntity
     */
    @GetMapping("/load/{username}")
    public ResponseEntity loadUserByUsername(@PathVariable String username) {
        UserVO user = userInfoService.loadUserByUsername(username);
        if (user == null) {
            log.info("Not found anything about user with username: {}.", username);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 保存用户
     *
     * @param user 用户
     * @return ResponseEntity
     */
    @PostMapping
    public Mono<UserInfo> saveUser(@RequestBody @Valid UserInfo user) {
        return userInfoService.save(user);
    }

    /**
     * 编辑用户
     *
     * @param user 用户
     * @return ResponseEntity
     */
    @PutMapping
    public Mono<UserInfo> modifyUser(@RequestBody @Valid UserInfo user) {
        return userInfoService.save(user);
    }
}
