package io.leafage.basic.assets.task;

import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticsTasksTest {

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsTasks statisticsTasks;

    @Test
    void everydayCreate() {
        Statistics statistics = new Statistics();
        statistics.setDate(LocalDate.now());
        given(this.statisticsService.create()).willReturn(Mono.just(statistics));

        statisticsTasks.everydayCreate();

        verify(statisticsService, times(1)).create();
    }

    @Test
    void everydayCreate_error() {
        given(this.statisticsService.create()).willThrow(new RuntimeException());

        statisticsTasks.everydayCreate();

        verify(statisticsService, times(1)).create();
    }

    @Test
    void yesterdayCalculate() {
        Statistics statistics = new Statistics();
        statistics.setDate(LocalDate.now());
        given(this.statisticsService.modify()).willReturn(Mono.just(statistics));

        statisticsTasks.yesterdayCalculate();

        verify(statisticsService, times(1)).modify();
    }

    @Test
    void yesterdayCalculate_error() {
        given(this.statisticsService.modify()).willThrow(new RuntimeException());

        statisticsTasks.yesterdayCalculate();

        verify(statisticsService, times(1)).modify();
    }
}