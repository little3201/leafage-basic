/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import top.abeille.basic.assets.document.AccountInfo;

/**
 * 账户信息repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface AccountInfoRepository extends ReactiveMongoRepository<AccountInfo, String> {
}
