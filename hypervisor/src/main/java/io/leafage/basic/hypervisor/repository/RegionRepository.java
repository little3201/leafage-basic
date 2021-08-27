package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.document.Region;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * region repository
 *
 * @author liwenqiang 2021-08-20 16:59
 **/
@Repository
public interface RegionRepository extends ReactiveMongoRepository<Region, ObjectId> {

    /**
     * 分页查询region
     *
     * @param pageable 分页参数
     * @return 有效数据集
     */
    Flux<Region> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 查询结果信息
     */
    Mono<Region> getByCodeAndEnabledTrue(Integer code);
}
