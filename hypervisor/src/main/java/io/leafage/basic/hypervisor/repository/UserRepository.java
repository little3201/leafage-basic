/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 用户repository
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {

    /**
     * 分页查询用户
     *
     * @param pageable 分页参数
     * @return 有效用户
     */
    Flux<User> findByEnabledTrue(Pageable pageable);

    /**
     * 根据账号查
     *
     * @param username 账号
     * @return 用户信息
     */
    Mono<User> getByUsername(String username);

    /**
     * 根据username/mobile/email查询enabled信息
     *
     * @param username 账号
     * @param phone    电话
     * @param email    邮箱
     * @return 用户信息
     */
    Mono<User> getByUsernameOrPhoneOrEmailAndEnabledTrue(String username, String phone, String email);

    /**
     * 是否已存在
     *
     * @param username 账号
     * @param phone    电话
     * @param email    邮箱
     * @return true-是，false-否
     */
    Mono<Boolean> existsByUsernameOrPhoneOrEmail(String username, String phone, String email);
}
