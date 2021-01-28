/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户组repository
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

}
