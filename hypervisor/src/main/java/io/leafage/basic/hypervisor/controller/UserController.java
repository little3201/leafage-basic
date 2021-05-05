/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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
     * 查询用户信息
     *
     * @return 查询的数据集，异常时返回204状态码
     */
    @GetMapping
    public ResponseEntity<Flux<UserVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Flux<UserVO> voFlux;
        try {
            voFlux = userService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Retrieve user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(voFlux);
    }

    /**
     * 根据传入的 username 查询信息
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 根据传入的 username 查询信息
     *
     * @param username 账号
     * @return 查询到的数据，异常时返回204状态码
     */
    @GetMapping("/{username}/details")
    public ResponseEntity<Mono<UserDetails>> fetchDetails(@PathVariable String username) {
        Mono<UserDetails> voMono;
        try {
            voMono = userService.fetchDetails(username);
        } catch (Exception e) {
            logger.error("Fetch user details occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(voMono);
    }

    /**
     * 根据组code查询关联用户信息
     *
     * @param code 组code
     * @return 查询到的数据集，异常时返回204状态码
     */
    @GetMapping("/{code}/relation")
    public ResponseEntity<Flux<UserVO>> relation(@PathVariable String code) {
        Flux<UserVO> voFlux;
        try {
            voFlux = userService.relation(code);
        } catch (Exception e) {
            logger.error("Retrieve group users occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(voFlux);
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(count);
    }

    /**
     * 根据传入的数据添加信息
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
     * 根据传入的username和要修改的数据，修改信息
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
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(voMono);
    }

    /**
     * 根据传入的username删除信息
     *
     * @param username 账号
     * @return 200状态码，异常时返回417状态码
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Mono<Void>> remove(@PathVariable String username) {
        try {
            userService.remove(username);
        } catch (Exception e) {
            logger.error("Remove user occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok().build();
    }
}
