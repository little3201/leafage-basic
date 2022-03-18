package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.vo.CommentVO;
import reactor.core.publisher.Flux;
import top.leafage.common.reactive.ReactiveBasicService;

/**
 * comment service
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
public interface CommentService extends ReactiveBasicService<CommentDTO, CommentVO, String> {

    /**
     * 查询
     *
     * @param code 帖子代码
     * @return 关联的评论
     */
    Flux<CommentVO> relation(String code);

    /**
     * 查询回复
     *
     * @param replier 回复代码
     * @return 回复的评论
     */
    Flux<CommentVO> replies(String replier);
}
