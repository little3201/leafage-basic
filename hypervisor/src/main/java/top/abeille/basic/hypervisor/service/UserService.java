/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import org.springframework.security.core.userdetails.UserDetails;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.vo.UserVO;
import top.abeille.common.basic.BasicService;

/**
 * java类描述
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends BasicService<UserDTO, UserVO> {

    UserDetails loadByUsername(String username);
}
