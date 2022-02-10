package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * notification repository
 *
 * @author liwenqiang 2022-02-10 13:49
 */
@Repository
public interface NotificationRepository extends ReactiveMongoRepository<Notification, ObjectId> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效数据集
     */
    Flux<Notification> findByReadAndEnabledTrue(Pageable pageable, boolean read);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 查询结果信息
     */
    Mono<Notification> getByCodeAndEnabledTrue(String code);
}
