package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDate;
import static org.mockito.BDDMockito.given;

/**
 * statistics service测试
 *
 * @author liwenqiang 2021/5/22 20:50
 */
@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private PostsRepository postsRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    void retrieve() {
        given(this.statisticsRepository.findByEnabledTrue(Mockito.any(PageRequest.class)))
                .willReturn(Flux.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.retrieve(0, 7)).expectNextCount(1).verifyComplete();
    }

    @Test
    void fetch() {
        given(this.statisticsRepository.getByDate(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.fetch()).expectNextCount(1).verifyComplete();
    }

    @Test
    void create_null() {
        Posts posts = new Posts();
        posts.setViewed(12);
        posts.setLikes(23);
        posts.setComment(2);
        given(this.postsRepository.findByEnabledTrue()).willReturn(Flux.just(posts));

        given(this.statisticsRepository.getByDate(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        given(this.statisticsRepository.insert(Mockito.any(Statistics.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.create()).expectNextCount(1).verifyComplete();
    }

    @Test
    void create() {
        Posts posts = new Posts();
        posts.setViewed(12);
        posts.setLikes(23);
        posts.setComment(2);
        given(this.postsRepository.findByEnabledTrue()).willReturn(Flux.just(posts));

        Statistics statistics = new Statistics(LocalDate.now().minusDays(1), 0, 0.0,
                0, 0.0, 0, 0.0);
        statistics.setId(new ObjectId());
        given(this.statisticsRepository.getByDate(Mockito.any(LocalDate.class)))
                .willReturn(Mono.just(statistics));

        given(this.statisticsRepository.insert(Mockito.any(Statistics.class)))
                .willReturn(Mono.just(Mockito.mock(Statistics.class)));

        StepVerifier.create(statisticsService.create()).expectNextCount(1).verifyComplete();
    }
}