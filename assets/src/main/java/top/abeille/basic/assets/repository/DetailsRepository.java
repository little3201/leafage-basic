/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Details;

/**
 * 内容信息repository
 *
 * @author liwenqiang 2020/2/26 18:29
 **/
@Repository
public interface DetailsRepository extends ReactiveMongoRepository<Details, String> {

    /**
     * 根据文章id查询enabled信息
     *
     * @param articleId 文章id
     * @return 文章内容
     */
    Mono<Details> findByArticleIdAndEnabledTrue(String articleId);
}
