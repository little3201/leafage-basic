/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.basic.hypervisor.vo.enter.UserEnter;
import top.abeille.basic.hypervisor.vo.outer.UserOuter;
import top.abeille.common.basic.BasicService;

/**
 * java类描述
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserInfoService extends BasicService<UserEnter, UserOuter> {

    /**
     * 根据username获取用户信息
     *
     * @param username 用户名
     * @return UserVO 用户认证信息
     */
    Mono<UserVO> loadUserByUsername(String username);

    /**
     * 根据userId获取用户信息
     *
     * @param userId 业务主键
     * @return UserOuter 用户信息
     */
    Mono<UserOuter> getByUserId(Long userId);
}
