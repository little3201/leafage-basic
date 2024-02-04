package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.PostStatistics;
import io.leafage.basic.assets.dto.PostStatisticsDTO;
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import io.leafage.basic.assets.service.PostStatisticsService;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * statistics service impl.
 *
 * @author wq li 2021/09/29 15:30
 **/
@Service
public class PostStatisticsServiceImpl implements PostStatisticsService {

    private final PostStatisticsRepository postStatisticsRepository;

    public PostStatisticsServiceImpl(PostStatisticsRepository postStatisticsRepository) {
        this.postStatisticsRepository = postStatisticsRepository;
    }

    @Override
    public Page<PostStatisticsVO> retrieve(int page, int size) {
        return postStatisticsRepository.findAll(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public PostStatisticsVO create(PostStatisticsDTO postStatisticsDTO) {
        PostStatistics postStatistics = new PostStatistics();
        BeanUtils.copyProperties(postStatisticsDTO, postStatistics);
        postStatistics = postStatisticsRepository.saveAndFlush(postStatistics);
        return this.convertOuter(postStatistics);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param postStatistics 信息
     * @return 输出转换后的vo对象
     */
    private PostStatisticsVO convertOuter(PostStatistics postStatistics) {
        PostStatisticsVO vo = new PostStatisticsVO();
        BeanUtils.copyProperties(postStatistics, vo);
        return vo;
    }

}
