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
     * 分页查询翻译信息
     *
     * @return 如果查询到数据，返回查询到的分页后的信息列表，否则返回空
     */
    @GetMapping
    public Flux<UserVO> retrieveUser() {
        Sort sort = super.initSortProperties();
        return userInfoService.retrieveAll(sort);
    }

    /**
     * 根据传入的业务id: userId 查询信息
     *
     * @param userId 业务id
     * @return 如果查询到数据，返回查询到的信息，否则返回404状态码
     */
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserVO>> fetchUser(@PathVariable String userId) {
        return userInfoService.fetchById(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 根据传入的数据添加信息
     *
     * @param userDTO 要添加的数据
     * @return 如果添加数据成功，返回添加后的信息，否则返回417状态码
     */
    @PostMapping
    public Mono<ResponseEntity<UserVO>> createUser(@RequestBody @Valid UserDTO userDTO) {
        return userInfoService.create(userDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 根据传入的业务id: userId 和要修改的数据，修改信息
     *
     * @param userId  业务id
     * @param userDTO 要修改的数据
     * @return 如果修改数据成功，返回修改后的信息，否则返回304状态码
     */
    @PutMapping("/{userId}")
    public Mono<ResponseEntity<UserVO>> modifyUser(@PathVariable String userId, @RequestBody @Valid UserDTO userDTO) {
        return userInfoService.modify(userId, userDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }
}
