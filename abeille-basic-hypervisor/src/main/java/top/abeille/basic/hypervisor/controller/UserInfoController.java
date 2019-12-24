/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.service.UserInfoService;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
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
    public Flux<UserVO> retrieveUser() {
        Sort sort = super.initSortProperties();
        return userInfoService.retrieveAll(sort);
    }

    /**
     * 用户查询——根据ID
     *
     * @param userId 用户ID
     * @return ResponseEntity
     */
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserVO>> fetchUser(@PathVariable Long userId) {
        return userInfoService.fetchById(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 用户查询——根据用户名
     *
     * @param username 用户名
     * @return ResponseEntity
     */
    @GetMapping("/load/{username}")
    public Mono<ResponseEntity<UserDetailsVO>> loadUserByUsername(@PathVariable String username) {
        return userInfoService.loadUserByUsername(username).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 保存用户
     *
     * @param userDTO 用户
     * @return ResponseEntity
     */
    @PostMapping
    public Mono<ResponseEntity<UserVO>> createUser(@RequestBody @Valid UserDTO userDTO) {
        return userInfoService.create(userDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 编辑用户
     *
     * @param userDTO 用户
     * @return ResponseEntity
     */
    @PutMapping("/{userId}")
    public Mono<ResponseEntity<UserVO>> modifyUser(@PathVariable Long userId, @RequestBody @Valid UserDTO userDTO) {
        return userInfoService.modify(userId, userDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }
}
