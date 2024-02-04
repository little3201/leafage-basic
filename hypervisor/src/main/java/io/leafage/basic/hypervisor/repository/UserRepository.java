/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * user repository.
 *
 * @author wq li 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return user
     */
    User getByUsername(String username);

    /**
     * 是否存在
     *
     * @param username 用户名
     * @return true-存在，false-否
     */
    boolean existsByUsername(String username);
}
