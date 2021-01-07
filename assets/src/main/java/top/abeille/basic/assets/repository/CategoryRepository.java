/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.entity.Category;

/**
 * 账户信息dao
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
