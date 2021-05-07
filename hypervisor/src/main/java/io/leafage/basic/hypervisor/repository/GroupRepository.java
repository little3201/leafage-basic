/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 分组信息 repository
 *
 * @author liwenqiang 2018/12/20 9:52
 **/
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * 根据code查询信息
     *
     * @param code 代码
     * @return 结果信息
     */
    Group findByCodeAndEnabledTrue(String code);
}
