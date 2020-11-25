/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.AccountInfo;

/**
 * 账户信息repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface AccountRepository extends ReactiveMongoRepository<AccountInfo, String> {

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 账户信息
     */
    Mono<AccountInfo> findByCodeAndEnabledTrue(String code);
}
