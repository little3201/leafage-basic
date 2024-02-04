/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * posts repository.
 *
 * @author wq li 2018/12/20 9:51
 **/
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 查询结果
     */
    Slice<Post> findByEnabledTrue(Pageable pageable);

    /**
     * 查询所有可用
     *
     * @return 查询结果
     */
    List<Post> findByEnabledTrue();

    /**
     * 统计
     *
     * @param categoryId 分类ID
     * @return 数量
     */
    long countByCategoryId(Long categoryId);

    /**
     * 是否已存在
     *
     * @param title 名称
     * @return true-是，false-否
     */
    boolean existsByTitle(String title);

    /**
     * 下一篇
     *
     * @param id 主键
     * @return 信息
     */
    Post getFirstByIdGreaterThanAndEnabledTrueOrderByIdAsc(Long id);

    /**
     * 上一篇
     *
     * @param id 主键
     * @return 信息
     */
    Post getFirstByIdLessThanAndEnabledTrueOrderByIdDesc(Long id);
}
