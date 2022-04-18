/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.entity.PostsContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * posts content repository.
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Repository
public interface PostsContentRepository extends JpaRepository<PostsContent, String> {

    /**
     * 根据postsId查询enabled信息
     *
     * @param postsId 帖子ID
     * @return 查询结果
     */
    PostsContent getByPostsIdAndEnabledTrue(Long postsId);
}
