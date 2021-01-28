/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.domain.UserDetails;
import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.UserVO;
import io.leafage.common.basic.BasicService;
import org.springframework.data.domain.Page;

/**
 * java类描述
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends BasicService<UserDTO, UserVO> {

    Page<UserVO> retrieves(int page, int size);

    UserDetails fetchDetails(String username);
}
