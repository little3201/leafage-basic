/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * posts repository
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
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 查询结果
     */
    Page<Posts> findByEnabledTrue(Pageable pageable);

    /**
     * 查询所有
     *
     * @return 查询结果
     */
    List<Posts> findByEnabledTrue();

    /**
     * 更新viewed
     *
     * @param code 唯一码
     */
    @Modifying
    @Query("update #{#entityName} set viewed = viewed + 1 where code = ?1")
    void flushViewed(String code);

    /**
     * 更新comment
     *
     * @param code 唯一码
     */
    @Modifying
    @Query("update #{#entityName} set comment = comment + 1 where code = ?1")
    void flushComment(String code);

    /**
     * 统计
     *
     * @param categoryId 分类ID
     * @return 数量
     */
    long countByCategoryId(long categoryId);

    /**
     * 根据code查询
     *
     * @param code 代码
     * @return 帖子信息
     */
    Posts getByCodeAndEnabledTrue(String code);

    /**
     * 是否已存在
     *
     * @param title 名称
     * @return true-是，false-否
     */
    boolean existsByTitle(String title);
}
