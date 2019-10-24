/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.vo.UserDetailsVO;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.BasicService;

/**
 * java类描述
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserInfoService extends BasicService<UserDTO, UserVO> {

    /**
     * 根据username获取用户信息
     *
     * @param username 用户名
     * @return UserDetailsVO 用户认证信息
     */
    UserDetailsVO loadUserByUsername(String username);
}
