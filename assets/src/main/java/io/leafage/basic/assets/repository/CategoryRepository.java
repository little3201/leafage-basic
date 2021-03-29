/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 分类dao
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 查询结果
     */
    Category findByCodeAndEnabledTrue(String code);

    /**
     * 根据codes批量查询
     *
     * @param codes 唯一标识
     * @return 分类集合
     */
    List<Category> findByCodeInAndEnabledTrue(Set<String> codes);
}
