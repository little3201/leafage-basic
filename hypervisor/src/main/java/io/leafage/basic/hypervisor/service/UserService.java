/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.UserVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * 用户信息Service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends ReactiveBasicService<UserDTO, UserVO> {

    /**
     * 查用户details
     *
     * @param username 账户
     * @return 用户details
     */
    Mono<UserDetails> fetchDetails(String username);

    /**
     * 根据关联查用户
     *
     * @param code 组
     * @return 用户信息
     */
    Flux<UserVO> relation(String code);
}
