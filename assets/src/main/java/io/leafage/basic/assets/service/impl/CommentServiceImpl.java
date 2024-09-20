/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.domain.Comment;
import io.leafage.basic.assets.dto.CommentDTO;
import io.leafage.basic.assets.repository.CommentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * comment service impl
 *
 * @author wq li
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * <p>Constructor for CommentServiceImpl.</p>
     *
     * @param commentRepository a {@link io.leafage.basic.assets.repository.CommentRepository} object
     * @param postRepository    a {@link io.leafage.basic.assets.repository.PostRepository} object
     */
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    /** {@inheritDoc} */
    @Override
    public Flux<CommentVO> comments(Long postId) {
        Assert.notNull(postId, "postId must not be null.");
        return commentRepository.findByPostIdAndReplierIsNull(postId).flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Override
    public Flux<CommentVO> replies(Long replier) {
        Assert.notNull(replier, "replier must not be null.");
        return commentRepository.findByReplier(replier).flatMap(this::convertOuter);
    }

    /** {@inheritDoc} */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<CommentVO> create(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        return commentRepository.save(comment).flatMap(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param comment 信息
     * @return 输出转换后的vo对象
     */
    private Mono<CommentVO> convertOuter(Comment comment) {
        return Mono.just(comment).flatMap(c -> commentRepository.countByReplier(c.getReplier())
                .switchIfEmpty(Mono.just(0L))
                .map(count -> {
                    CommentVO vo = new CommentVO();
                    BeanUtils.copyProperties(c, vo);
                    vo.setLastModifiedDate(c.getLastModifiedDate().orElse(null));
                    vo.setCount(count);
                    return vo;
                }));
    }

}
