/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.UserVO;
import io.leafage.common.basic.BasicService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 用户信息Service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends BasicService<UserDTO, UserVO> {

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
