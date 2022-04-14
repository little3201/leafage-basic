package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.AccessLog;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * record repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface AccessLogRepository extends ReactiveMongoRepository<AccessLog, ObjectId> {

    /**
     * 查询
     *
     * @return 有效帖子
     */
    Flux<AccessLog> findByEnabledTrue(Pageable pageable);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
