/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 权限 repository
 *
 * @author liwenqiang 2018/12/17 19:37
 **/
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    /**
     * 根据code查询
     *
     * @param code 唯一标识
     * @return 信息
     */
    Authority findByCodeAndEnabledTrue(String code);

    /**
     * 根据 ids 查询
     *
     * @param ids 主键集合
     * @return 信息
     */
    List<Authority> findByIdIn(@NotNull List<Long> ids);
}
