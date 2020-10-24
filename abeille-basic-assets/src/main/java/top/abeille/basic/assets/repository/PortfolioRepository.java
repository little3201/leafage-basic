/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.PortfolioInfo;

/**
 * 作品集信息repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface PortfolioRepository extends ReactiveMongoRepository<PortfolioInfo, String> {

    Mono<PortfolioInfo> findByCodeAndEnabledTrue(String code);
}
