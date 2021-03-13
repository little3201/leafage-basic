/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.service.UserService;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.http.HttpStatus;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询用户信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<UserVO> retrieve(@RequestParam int page, @RequestParam int size) {
        return userService.retrieve(page, size);
    }

    /**
     * 根据传入的 username 查询信息
     *
     * @param username 用户账号
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}")
    public Mono<UserVO> fetch(@PathVariable String username) {
        return userService.fetch(username);
    }

    /**
     * 根据传入的 username 查询信息
     *
     * @param username 用户账号
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}/details")
    public Mono<UserDetails> fetchDetails(@PathVariable String username) {
        return userService.fetchDetails(username);
    }

    /**
     * 根据组code查询关联用户信息
     *
     * @param code 组code
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{code}/relation")
    public Flux<UserVO> relation(@PathVariable String code) {
        return userService.relation(code);
    }

    /**
     * 统计记录数
     *
     * @return 记录数
     */
    @GetMapping("/count")
    public Mono<Long> count() {
        return userService.count();
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param userDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserVO> createUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.create(userDTO);
    }

    /**
     * 根据传入的username和要修改的数据，修改信息
     *
     * @param username 账号
     * @param userDTO  要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<UserVO> modify(@PathVariable String username, @RequestBody @Valid UserDTO userDTO) {
        return userService.modify(username, userDTO);
    }

    /**
     * 根据传入的username删除信息
     *
     * @param username 账号
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> remove(@PathVariable String username) {
        return userService.remove(username);
    }
}
