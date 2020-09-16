/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.service.UserService;
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
public class UserController extends AbstractController {

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
    public Flux<UserVO> retrieveUser() {
        return userService.retrieveAll();
    }

    /**
     * 根据传入的 username 查询信息
     *
     * @param username 用户账号
     * @return 如果查询到数据，返回查询到的信息，否则返回204状态码
     */
    @GetMapping("/{username}")
    public Mono<UserDetails> loadByUsername(@PathVariable String username) {
        return userService.loadByUsername(username);
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param userDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
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
    @PutMapping("/{code}")
    public Mono<UserVO> modifyUser(@PathVariable String username, @RequestBody @Valid UserDTO userDTO) {
        return userService.modify(username, userDTO);
    }

    /**
     * 根据传入的username删除信息
     *
     * @param username 账号
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{username}")
    public Mono<Void> removeUser(@PathVariable String username) {
        return userService.remove(username);
    }
}
