/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户信息repository
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsernameAndEnabledTrue(String username);

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @param phone    电话
     * @param email    邮箱
     * @return 用户信息
     */
    User getByUsernameOrPhoneOrEmailAndEnabledTrue(String username, String phone, String email);
}
