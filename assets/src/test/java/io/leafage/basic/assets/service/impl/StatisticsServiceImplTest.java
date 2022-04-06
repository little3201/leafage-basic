package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.entity.Posts;
import io.leafage.basic.assets.entity.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.vo.StatisticsVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.BDDMockito.given;

/**
 * statistics 接口测试
 *
 * @author liwenqiang 2021/12/7 17:55
 **/
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
        Statistics statistics = new Statistics();
        statistics.setComments(12);
        statistics.setLikes(23);
        statistics.setViewed(121);
        Page<Statistics> page = new PageImpl<>(List.of(statistics));
        given(this.statisticsRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(page);

        Page<StatisticsVO> voPage = statisticsService.retrieve(0, 2);
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        Posts posts = new Posts();
        posts.setComments(19);
        posts.setLikes(90);
        posts.setViewed(567);
        given(this.postsRepository.findByEnabledTrue()).willReturn(List.of(posts));

        Statistics ys = new Statistics();
        ys.setComments(12);
        ys.setLikes(23);
        ys.setViewed(121);
        given(this.statisticsRepository.getByDate(LocalDate.now().minusDays(2))).willReturn(ys);

        Statistics bys = new Statistics();
        bys.setComments(10);
        bys.setLikes(9);
        bys.setViewed(78);
        given(this.statisticsRepository.getByDate(LocalDate.now().minusDays(3))).willReturn(bys);

        Statistics statistics = new Statistics();
        statistics.setOverViewed(9.0);
        statistics.setOverLikes(9.3);
        statistics.setOverComment(9.4);
        given(this.statisticsRepository.saveAndFlush(Mockito.any(Statistics.class))).willReturn(statistics);

        Statistics st = statisticsService.create();
        Assertions.assertNotNull(st);
    }

    @Test
    void create_zero() {
        Posts posts = new Posts();
        posts.setComments(19);
        posts.setLikes(90);
        posts.setViewed(567);
        given(this.postsRepository.findByEnabledTrue()).willReturn(List.of(posts));

        Statistics statistics = new Statistics();
        statistics.setComments(12);
        statistics.setLikes(23);
        statistics.setViewed(121);
        given(this.statisticsRepository.getByDate(LocalDate.now().minusDays(2))).willReturn(statistics);

        given(this.statisticsRepository.getByDate(LocalDate.now().minusDays(3))).willReturn(statistics);

        statistics.setOverViewed(9.0);
        statistics.setOverLikes(9.3);
        statistics.setOverComment(9.4);
        given(this.statisticsRepository.saveAndFlush(Mockito.any(Statistics.class))).willReturn(statistics);

        Statistics st = statisticsService.create();
        Assertions.assertNotNull(st);
    }
}