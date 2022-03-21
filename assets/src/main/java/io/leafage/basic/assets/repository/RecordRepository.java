package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Record;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * record repository
 *
 * @author liwenqiang 2018/12/20 9:51
 **/
@Repository
public interface RecordRepository extends ReactiveMongoRepository<Record, ObjectId> {

    /**
     * 查询
     *
     * @return 有效帖子
     */
    Flux<Record> findByEnabledTrue();
}
