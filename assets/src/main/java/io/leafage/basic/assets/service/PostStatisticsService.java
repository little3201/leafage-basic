package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.PostStatisticsDTO;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * statistics service.
 *
 * @author liwenqiang 2021/09/29 14:32
 **/
public interface PostStatisticsService extends ServletBasicService<PostStatisticsDTO, PostStatisticsVO> {

    /**
     * 分页查询
     *
     * @param page 分页
     * @param size 大小
     * @return 结果集
     */
    Page<PostStatisticsVO> retrieve(int page, int size);

}
