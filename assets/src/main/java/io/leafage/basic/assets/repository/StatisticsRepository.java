package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.document.Statistics;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@Repository
public interface StatisticsRepository extends ReactiveMongoRepository<Statistics, ObjectId> {

    Mono<Statistics> getByDate(LocalDate date);
}
