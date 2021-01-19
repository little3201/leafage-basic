/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.entity.Category;

import java.util.List;
import java.util.Set;

/**
 * 分类dao
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCodeAndEnabledTrue(String code);

    /**
     * 根据codes批量查询
     *
     * @param codes 唯一标识
     * @return 分类集合
     */
    List<Category> findByCodeInAndEnabledTrue(Set<String> codes);
}
