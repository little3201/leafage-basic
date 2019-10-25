/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.entity.ArticleInfo;

/**
 * 账户信息dao
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface ArticleInfoRepository extends ReactiveMongoRepository<ArticleInfo, String> {
}
