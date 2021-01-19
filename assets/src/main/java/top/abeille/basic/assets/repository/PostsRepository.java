/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.abeille.basic.assets.entity.Posts;

/**
 * 帖子信息dao
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    /**
     * 根据code查询
     *
     * @param code 唯一标识
     * @return 信息
     */
    Posts findByCodeAndEnabledTrue(String code);

    /**
     * 更新viewed
     *
     * @param id 主键ID
     */
    @Transactional
    @Modifying
    @Query("update #{#entityName} set viewed = viewed + 1 where id = ?1")
    void flushViewed(long id);

    /**
     * 统计
     *
     * @param categoryId 分类ID
     * @return 数量
     */
    int countByCategoryId(long categoryId);

}
