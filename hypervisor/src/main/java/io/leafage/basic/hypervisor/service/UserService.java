/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.AccountVO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import top.leafage.common.servlet.ServletBasicService;

/**
 * 用户信息service
 *
 * @author liwenqiang 2018/7/28 0:29
 **/
public interface UserService extends ServletBasicService<UserDTO, UserVO, String>, UserDetailsService {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 查询结果
     */
    Page<AccountVO> retrieve(int page, int size, String sort);

}
