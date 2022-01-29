/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * account repository.
 *
 * @author liwenqiang 2022/1/26 15:20
 **/
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 信息
     */
    Page<Account> findByEnabledTrue(Pageable pageable);

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    Account getByUsernameAndEnabledTrue(String username);

    /**
     * 是否存在
     *
     * @param username 用户名
     * @return true-存在，false-否
     */
    boolean existsByUsername(String username);
}
