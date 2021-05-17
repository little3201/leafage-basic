package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Statistics;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends ReactiveMongoRepository<Statistics, ObjectId> {
}
