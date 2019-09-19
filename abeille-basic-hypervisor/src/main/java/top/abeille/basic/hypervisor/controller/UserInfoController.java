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
import java.util.Objects;

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
    public ResponseEntity fetchUsers() {
        Flux<UserInfo> infoFlux = userInfoService.findAll();
        if (Objects.isNull(infoFlux)) {
            log.info("Not found anything about user.");
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(infoFlux);
    }

    /**
     * 用户查询——根据ID
     *
     * @param userId 用户ID
     * @return ResponseEntity
     */
    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable String userId) {
        Mono<UserInfo> infoMono = userInfoService.getByUserId(userId);
        if (Objects.isNull(infoMono)) {
            log.info("Not found with userId: {}.", userId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(infoMono);
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
        if (Objects.isNull(user)) {
            log.info("Not found with username: {}.", username);
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
    public ResponseEntity saveUser(@RequestBody @Valid UserInfo user) {
        Mono<UserInfo> infoMono = userInfoService.save(user);
        if (Objects.isNull(infoMono)) {
            log.error("Save user occurred error.");
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 编辑用户
     *
     * @param user 用户
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity modifyUser(@RequestBody @Valid UserInfo user) {
        if (Objects.isNull(user.getId())) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Mono<UserInfo> infoMono = userInfoService.save(user);
        if (Objects.isNull(infoMono)) {
            log.error("Modify user occurred error.");
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
