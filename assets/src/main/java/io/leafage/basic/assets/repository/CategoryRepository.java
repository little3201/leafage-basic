/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * category repository.
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 查询结果
     */
    Page<Category> findByEnabledTrue(Pageable pageable);

    /**
     * 根据id查询
     *
     * @param id 唯一标识
     * @return 查询结果
     */
    Category getByCodeAndEnabledTrue(Long id);

    /**
     * 是否已存在
     *
     * @param name 名称
     * @return true-是，false-否
     */
    boolean existsByName(String name);
}
