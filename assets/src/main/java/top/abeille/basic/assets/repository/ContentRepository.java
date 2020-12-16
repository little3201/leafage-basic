/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.document.Content;

/**
 * 文章内容信息dao
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface ContentRepository extends MongoRepository<Content, String> {

    Content findByPostsIdAndEnabledTrue(Long postsId);
}
