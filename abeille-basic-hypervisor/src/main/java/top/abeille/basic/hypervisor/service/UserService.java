/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.BasicService;

/**
 * 用户信息Service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends BasicService<UserDTO, UserVO> {

    /**
     * 根据username 查询账户信息
     *
     * @param username 账号
     * @return UserDetails 账户信息
     */
    Mono<UserDetails> loadByUsername(String username);
}
