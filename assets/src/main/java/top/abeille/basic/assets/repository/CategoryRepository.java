/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.Category;

/**
 * 类别信息repository
 *
 * @author liwenqiang 2020/2/13 22:01
 **/
@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 类别信息
     */
    Mono<Category> findByCodeAndEnabledTrue(String code);
}
