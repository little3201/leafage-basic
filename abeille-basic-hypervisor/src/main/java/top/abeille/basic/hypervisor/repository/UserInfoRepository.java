/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import top.abeille.basic.hypervisor.entity.UserInfo;

/**
 * 用户信息(userInfo)dao接口
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
public interface UserInfoRepository extends ReactiveMongoRepository<UserInfo, Long> {
}
