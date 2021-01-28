/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.entity.PostsContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 内容信息dao
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Repository
public interface PostsContentRepository extends JpaRepository<PostsContent, String> {

    PostsContent findByPostsIdAndEnabledTrue(Long postsId);
}
