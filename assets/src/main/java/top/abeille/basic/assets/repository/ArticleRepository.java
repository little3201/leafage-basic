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

    /**
     * 根据起始和结束日期查询文章数
     *
     * @param startDate 起始日期
     * @param deadline  截至日期
     * @return 有效文章数
     */
    Mono<Long> countByModifyTimeBetween(LocalDate startDate, LocalDate deadline);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 文章信息
     */
    Mono<ArticleInfo> findByCodeAndEnabledTrue(String code);
}
