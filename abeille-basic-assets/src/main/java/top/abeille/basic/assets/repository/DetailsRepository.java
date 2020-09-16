/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.DetailsInfo;

/**
 * 内容信息repository
 *
 * @author liwenqiang 2020/2/26 18:29
 **/
@Repository
public interface DetailsRepository extends ReactiveMongoRepository<DetailsInfo, String> {

    Mono<DetailsInfo> findByArticleIdAndEnabledTrue(String articleId);
}
