/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.hypervisor.entity.Authority;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * 权限资源dao
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

    /**
     * 根据codes查询
     *
     * @param codes 代码集合
     * @return 信息
     */
    List<Authority> findByCodeInAndEnabledTrue(Collection<String> codes);
}
