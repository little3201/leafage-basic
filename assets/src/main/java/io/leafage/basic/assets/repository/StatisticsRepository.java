package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Statistics;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * statistics repository
 *
 * @author liwenqiang 2020-02-13 22:01
 **/
@Repository
public interface StatisticsRepository extends ReactiveMongoRepository<Statistics, ObjectId> {

    /**
     * 根据data查询当日数据
     *
     * @param date 日期
     * @return 统计数据
     */
    Mono<Statistics> getByModifyTime(LocalDate date);

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效帖子
     */
    Flux<Statistics> findByEnabledTrue(Pageable pageable);

    /**
     * 统计
     *
     * @return 记录数
     */
    Mono<Long> countByEnabledTrue();
}
