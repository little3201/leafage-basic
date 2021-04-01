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

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  大小
     * @param order 排序字段
     * @return 查询结果
     */
    Page<UserVO> retrieve(int page, int size, String order);

    /**
     * 查询details信息, for security
     *
     * @param username 账户
     * @return 查询结果
     */
    UserDetails fetchDetails(String username);
}
