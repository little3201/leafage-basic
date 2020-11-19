/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import top.abeille.basic.hypervisor.document.UserInfo;
import top.abeille.basic.hypervisor.vo.UserTidyVO;

/**
 * 用户信息(userInfo)dao接口
 *
 * @author liwenqiang 2018/7/27 17:50
 **/
@Repository
public interface UserRepository extends ReactiveMongoRepository<UserInfo, String> {

    /**
     * 根据username查询enabled信息
     *
     * @param username 账户
     * @return 用户信息
     */
    Mono<UserTidyVO> findByUsername(String username);
}
