package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.PostStatistics;
import io.leafage.basic.assets.dto.PostStatisticsDTO;
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
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * statistics 接口测试
 *
 * @author wq li 2021/12/7 17:55
 **/
@ExtendWith(MockitoExtension.class)
class PostPostStatisticsServiceImplTest {

    @Mock
    private PostStatisticsRepository postStatisticsRepository;

    @InjectMocks
    private PostStatisticsServiceImpl statisticsService;

    private PostStatisticsDTO postStatisticsDTO;

    @BeforeEach
    void init() {
        postStatisticsDTO = new PostStatisticsDTO();
        postStatisticsDTO.setPostId(1L);
        postStatisticsDTO.setComments(23);
        postStatisticsDTO.setLikes(213);
        postStatisticsDTO.setViewed(2542);
    }


    @Test
    void retrieve() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<PostStatistics> page = new PageImpl<>(List.of(Mockito.mock(PostStatistics.class)), pageable, 2L);
        given(this.postStatisticsRepository.findAll(PageRequest.of(0, 2))).willReturn(page);

        Page<PostStatisticsVO> voPage = statisticsService.retrieve(0, 2);
        Assertions.assertNotNull(voPage.getContent());
    }

    @Test
    void create() {
        given(this.postStatisticsRepository.saveAndFlush(Mockito.any(PostStatistics.class))).willReturn(Mockito.mock(PostStatistics.class));

        PostStatisticsVO vo = statisticsService.create(postStatisticsDTO);

        verify(this.postStatisticsRepository, times(1)).saveAndFlush(Mockito.any(PostStatistics.class));
        Assertions.assertNotNull(vo);
    }

    @Test
    void create_zero() {
        given(this.postStatisticsRepository.saveAndFlush(Mockito.any(PostStatistics.class))).willReturn(Mockito.mock(PostStatistics.class));

        PostStatisticsVO vo = statisticsService.create(postStatisticsDTO);
        Assertions.assertNotNull(vo);
    }
}