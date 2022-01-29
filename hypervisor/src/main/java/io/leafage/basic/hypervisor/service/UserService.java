/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.UserVO;
import top.leafage.common.servlet.ServletBasicService;

/**
 * user service.
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends ServletBasicService<UserDTO, UserVO, String> {
}
