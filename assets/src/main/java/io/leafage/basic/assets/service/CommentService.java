package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;
import java.util.List;

/**
 * comment service.
 *
 * @author liwenqiang 2021/09/29 14:34
 **/
public interface CommentService extends ServletBasicService<CommentDTO, CommentVO, String> {

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 大小
     * @return 查询结果
     */
    Page<CommentVO> retrieve(int page, int size);

    /**
     * 根据posts查询
     *
     * @param code 帖子代码
     * @return 关联的评论
     */
    List<CommentVO> posts(String code);
}
