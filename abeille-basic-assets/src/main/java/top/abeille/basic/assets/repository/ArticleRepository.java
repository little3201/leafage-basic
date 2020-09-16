/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.ArticleInfo;

import java.time.LocalDate;

/**
 * 文章信息repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface ArticleRepository extends ReactiveMongoRepository<ArticleInfo, String> {

    Mono<Long> countByModifyTimeBetween(LocalDate startDate, LocalDate deadline);

    Mono<ArticleInfo> findByCodeAndEnableTrue(String code);
}
