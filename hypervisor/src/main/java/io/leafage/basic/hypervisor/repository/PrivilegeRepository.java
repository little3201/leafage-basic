/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * privilege repository.
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    /**
     * 查询所有可用数据
     *
     * @return 信息
     */
    List<Privilege> findByEnabledTrue();
}
