/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.TopicInfo;

/**
 * 话题信息repository
 *
 * @author liwenqiang 2020/2/13 22:01
 **/
@Repository
public interface TopicRepository extends ReactiveMongoRepository<TopicInfo, String> {

    Mono<TopicInfo> findByCodeAndEnabledTrue(String code);
}
