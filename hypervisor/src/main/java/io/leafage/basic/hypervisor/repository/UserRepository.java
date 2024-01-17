/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * user repository.
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 信息
     */
    Page<User> findByEnabledTrue(Pageable pageable);

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return user
     */
    User getByUsernameAndEnabledTrue(String username);

    /**
     * 是否存在
     *
     * @param username 用户名
     * @param phone    电话
     * @param email    邮箱
     * @return true-存在，false-否
     */
    boolean existsByUsernameOrPhoneOrEmail(String username, String phone, String email);
}
