/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.entity.PostsContent;

/**
 * 内容信息dao
 *
 * @author liwenqiang  2020-12-03 22:59
 **/
@Repository
public interface PostsContentRepository extends JpaRepository<PostsContent, String> {

    PostsContent findByPostsIdAndEnabledTrue(Long postsId);
}
