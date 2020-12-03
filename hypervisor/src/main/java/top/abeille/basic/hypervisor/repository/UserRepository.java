/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.User;

/**
 * 用户信息(userInfo)dao接口
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    /**
     * 根据username/mobile/email查询enabled信息
     *
     * @param username 账号
     * @param phone    电话
     * @param email    邮箱
     * @return 用户信息
     */
    Mono<User> findByUsernameOrPhoneOrEmailAndEnabledTrue(String username, String phone, String email);
}
