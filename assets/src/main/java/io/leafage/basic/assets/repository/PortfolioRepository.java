/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Portfolio;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * portfolio repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface PortfolioRepository extends ReactiveMongoRepository<Portfolio, ObjectId> {

    /**
     * 分页查询作品集
     *
     * @param pageable 分页参数
     * @return 有效作品集
     */
    Flux<Portfolio> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 作品信息
     */
    Mono<Portfolio> getByCodeAndEnabledTrue(String code);
}
