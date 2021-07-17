package io.leafage.basic.assets.service;

import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.vo.CommentVO;
import reactor.core.publisher.Flux;
import top.leafage.common.reactive.ReactiveBasicService;

public interface CommentService extends ReactiveBasicService<CommentDTO, CommentVO> {

    /**
     * 根据posts查询
     *
     * @param code 帖子代码
     * @return 关联的评论
     */
    Flux<CommentVO> findByPosts(String code);
}
