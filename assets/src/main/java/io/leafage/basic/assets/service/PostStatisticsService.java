package io.leafage.basic.assets.service;

import io.leafage.basic.assets.domain.PostStatistics;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.springframework.data.domain.Page;

/**
 * statistics service.
 *
 * @author liwenqiang 2021/09/29 14:32
 **/
public interface PostStatisticsService {

    /**
     * 分页查询
     *
     * @param page 分页
     * @param size 大小
     * @return 结果集
     */
    Page<PostStatisticsVO> retrieve(int page, int size);

    /**
     * 浏览量记录
     *
     * @return 记录结果
     */
    PostStatistics create();
}
