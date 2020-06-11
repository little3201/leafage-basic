/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.service.UserService;
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
     * 分页查询翻译信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Mono<ServerResponse> retrieveUser() {
        return ServerResponse.ok().body(BodyInserters.fromValue(userService.retrieveAll()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }

    /**
     * 根据传入的业务id: businessId 查询信息
     *
     * @param businessId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{businessId}")
    public Mono<ServerResponse> fetchUser(@PathVariable String businessId) {
        return ServerResponse.ok().body(BodyInserters.fromValue(userService.fetchByBusinessId(businessId)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param userDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ServerResponse> createUser(@RequestBody @Valid UserDTO userDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(userService.create(userDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build());
    }

    /**
     * 根据传入的业务id: businessId 和要修改的数据，修改信息
     *
     * @param businessId 业务id
     * @param userDTO    要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{businessId}")
    public Mono<ServerResponse> modifyUser(@PathVariable String businessId, @RequestBody @Valid UserDTO userDTO) {
        return ServerResponse.ok().body(BodyInserters.fromValue(userService.modify(businessId, userDTO)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_MODIFIED).build());
    }

    /**
     * 根据传入的业务id: businessId 删除信息
     *
     * @param businessId 业务id
     * @return 如果删除成功，返回200状态码，否则返回417状态码
     */
    @DeleteMapping("/{businessId}")
    public Mono<ServerResponse> removeUser(@PathVariable String businessId) {
        return ServerResponse.ok().body(BodyInserters.fromValue(userService.removeById(businessId)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build());
    }
}
