/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.UserInfo;
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
     * 根据业务ID查询信息
     *
     * @param businessId 业务ID
     * @return 数据库映射对象
     */
    Mono<UserInfo> fetchInfo(String businessId);
}
