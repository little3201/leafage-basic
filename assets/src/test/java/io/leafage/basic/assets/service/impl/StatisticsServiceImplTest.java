package io.leafage.basic.assets.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.dto.StatisticsDTO;
import io.leafage.basic.assets.repository.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;

/**
 * statistics service test
 *
 * @author liwenqiang 2021/5/22 20:50
 */
@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    void create() {
        given(this.statisticsRepository.insert(Mockito.any(Statistics.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.create(Mockito.mock(StatisticsDTO.class))).expectNextCount(1).verifyComplete();
    }

    @Test
    void increase() {
        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("date").is(LocalDate.now())),
                new Update().inc(StatisticsFieldEnum.VIEWED.value, 1), Statistics.class))
                .willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        given(this.statisticsRepository.getByModifyTime(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.increase(LocalDate.now(), StatisticsFieldEnum.VIEWED))
                .expectNextCount(1).verifyComplete();
    }
}