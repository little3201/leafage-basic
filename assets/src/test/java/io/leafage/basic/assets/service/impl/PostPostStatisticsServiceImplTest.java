package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.PostStatistics;
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * statistics 接口测试
 *
 * @author liwenqiang 2021/12/7 17:55
 **/
@ExtendWith(MockitoExtension.class)
class PostPostStatisticsServiceImplTest {

    @Mock
    private PostStatisticsRepository postStatisticsRepository;

    @InjectMocks
    private PostStatisticsServiceImpl statisticsService;

    PostStatistics postStatistics = new PostStatistics();

    @BeforeEach
    void init() {
        postStatistics = new PostStatistics();
        postStatistics.setComments(12);
        postStatistics.setLikes(23);
        postStatistics.setViewed(121);
    }

    @Test
    void retrieve() {
        Page<PostStatistics> page = new PageImpl<>(List.of(postStatistics));
        given(this.postStatisticsRepository.findByEnabledTrue(PageRequest.of(0, 2))).willReturn(page);

        Page<PostStatisticsVO> voPage = statisticsService.retrieve(0, 2);
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        given(this.postStatisticsRepository.getByDate(LocalDate.now().minusDays(2))).willReturn(postStatistics);

        given(this.postStatisticsRepository.getByDate(LocalDate.now().minusDays(3))).willReturn(postStatistics);

        given(this.postStatisticsRepository.saveAndFlush(Mockito.any(PostStatistics.class))).willReturn(postStatistics);

        PostStatistics st = statisticsService.create();

        verify(this.postStatisticsRepository, times(1)).saveAndFlush(Mockito.any(PostStatistics.class));
        Assertions.assertNotNull(st);
    }

    @Test
    void create_zero() {
        given(this.postStatisticsRepository.getByDate(LocalDate.now().minusDays(2))).willReturn(postStatistics);

        given(this.postStatisticsRepository.getByDate(LocalDate.now().minusDays(3))).willReturn(postStatistics);

        given(this.postStatisticsRepository.saveAndFlush(Mockito.any(PostStatistics.class))).willReturn(postStatistics);

        PostStatistics st = statisticsService.create();
        Assertions.assertNotNull(st);
    }
}