/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.hypervisor.entity.User;

/**
 * 用户信息(userInfo)dao接口
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
    User findByUsernameAndEnabledTrue(String username);

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @param phone    电话
     * @param email    邮箱
     * @return 用户信息
     */
    User findByUsernameOrPhoneOrEmailAndEnabledTrue(String username, String phone, String email);
}
