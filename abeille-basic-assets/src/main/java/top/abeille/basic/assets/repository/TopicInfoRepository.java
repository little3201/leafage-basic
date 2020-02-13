/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.document.ArticleInfo;
import top.abeille.basic.assets.document.TopicInfo;

/**
 * 账户信息dao
 *
 * @author liwenqiang 2020/2/13 22:01
 **/
@Repository
public interface TopicInfoRepository extends ReactiveMongoRepository<TopicInfo, String> {
}
