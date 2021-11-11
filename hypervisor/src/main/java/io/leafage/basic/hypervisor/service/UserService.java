/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.vo.UserDetailVO;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.UserVO;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 用户信息Service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends ReactiveBasicService<UserDTO, UserVO, String> {

    /**
     * 查用户details
     *
     * @param username 账户
     * @return 用户details
     */
    Mono<UserDetailVO> details(String username);

}
