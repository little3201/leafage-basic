package io.leafage.basic.assets.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.repository.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
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
    void retrieve() {
        given(this.statisticsRepository.findByEnabledTrue(Mockito.any(PageRequest.class)))
                .willReturn(Flux.just(Mockito.mock(Statistics.class)));

        given(this.statisticsRepository.countByEnabledTrue()).willReturn(Mono.just(Mockito.anyLong()));

        StepVerifier.create(statisticsService.retrieve(0, 7)).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        given(this.statisticsRepository.insert(Mockito.any(Statistics.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.create()).expectNextCount(1).verifyComplete();
    }

    @Test
    void modify() {
        Statistics bys = new Statistics();
        bys.setDate(LocalDate.now().minusDays(3));
        bys.setLikes(123);
        bys.setOverLikes(30.4);
        bys.setComments(12);
        bys.setOverComments(33.4);
        bys.setViewed(3234);
        bys.setOverViewed(3.23);
        bys.setDownloads(234);
        bys.setOverDownloads(232.32);
        given(this.statisticsRepository.getByDate(LocalDate.now().minusDays(1L)))
                .willReturn(Mono.just(bys));

        Statistics tda = new Statistics();
        tda.setDate(LocalDate.now().minusDays(2));
        tda.setLikes(198);
        tda.setOverLikes(bys.getOverLikes() - 1.0);
        tda.setComments(27);
        tda.setOverComments(bys.getOverComments() - 2.0);
        tda.setViewed(8390);
        tda.setOverViewed(bys.getOverViewed() - 0.3);
        given(this.statisticsRepository.getByDate(LocalDate.now().minusDays(2L)))
                .willReturn(Mono.just(tda));

        given(this.statisticsRepository.save(Mockito.any(Statistics.class))).willReturn(Mono.empty());

        StepVerifier.create(statisticsService.modify()).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.statisticsRepository.getByDate(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.fetch()).expectNextCount(1).verifyComplete();
    }

    @Test
    void increase() {
        given(this.reactiveMongoTemplate.upsert(Query.query(Criteria.where("date").is(LocalDate.now())),
                new Update().inc(StatisticsFieldEnum.VIEWED.value, 1), Statistics.class))
                .willReturn(Mono.just(Mockito.mock(UpdateResult.class)));

        given(this.statisticsRepository.getByDate(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.increase(LocalDate.now(), StatisticsFieldEnum.VIEWED))
                .expectNextCount(1).verifyComplete();
    }
}