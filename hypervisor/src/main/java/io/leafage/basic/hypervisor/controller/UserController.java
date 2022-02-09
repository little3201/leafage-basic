/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

/**
 * 用户信息Controller
 *
 * @author liwenqiang 2018/8/2 21:02
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 根据 username 查询
     *
     * @param username 用户账号
     * @return 查询的数据，异常时返回204状态码
     */
    @GetMapping("/{username}")
    public ResponseEntity<Mono<UserVO>> fetch(@PathVariable String username) {
        Mono<UserVO> voMono;
        try {
            voMono = userService.fetch(username);
        } catch (Exception e) {
            logger.error("Fetch user occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 统计记录数
     *
     * @return 查询到的数据，异常时返回204状态码
     */
    @GetMapping("/count")
    public ResponseEntity<Mono<Long>> count() {
        Mono<Long> count;
        try {
            count = userService.count();
        } catch (Exception e) {
            logger.error("Count user occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 是否已存在
     *
     * @param username 用户名
     * @return true-是，false-否
     */
    @GetMapping("/{username}/exist")
    public ResponseEntity<Mono<Boolean>> exist(@PathVariable String username) {
        Mono<Boolean> existsMono;
        try {
            existsMono = userService.exist(username);
        } catch (Exception e) {
            logger.error("Check user is exist an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(existsMono);
    }

    /**
     * 添加信息
     *
     * @param userDTO 要添加的数据
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<Mono<UserVO>> create(@RequestBody @Valid UserDTO userDTO) {
        Mono<UserVO> voMono;
        try {
            voMono = userService.create(userDTO);
        } catch (Exception e) {
            logger.error("Create user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(voMono);
    }

    /**
     * 修改信息
     *
     * @param username 账号
     * @param userDTO  要修改的数据
     * @return 修改后的信息，异常时返回304状态码
     */
    @PutMapping("/{username}")
    public ResponseEntity<Mono<UserVO>> modify(@PathVariable String username, @RequestBody @Valid UserDTO userDTO) {
        Mono<UserVO> voMono;
        try {
            voMono = userService.modify(username, userDTO);
        } catch (Exception e) {
            logger.error("Modify user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(voMono);
    }

    /**
     * 删除信息
     *
     * @param username 账号
     * @return 200状态码，异常时返回417状态码
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Mono<Void>> remove(@PathVariable String username) {
        Mono<Void> voidMono;
        try {
            voidMono = userService.remove(username);
        } catch (Exception e) {
            logger.error("Remove user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(voidMono);
    }

}
