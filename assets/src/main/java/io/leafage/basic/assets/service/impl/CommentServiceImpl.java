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
import io.leafage.basic.assets.repository.PostStatisticsRepository;
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * comment service impl.
 *
 * @author wq li 2021/09/29 15:10
 **/
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostStatisticsRepository postStatisticsRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostStatisticsRepository postStatisticsRepository) {
        this.commentRepository = commentRepository;
        this.postStatisticsRepository = postStatisticsRepository;
    }

    @Override
    public Page<CommentVO> retrieve(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size)).map(this::convertOuter);
    }

    @Override
    public List<CommentVO> relation(Long id) {
        Assert.notNull(id, "comment id must not be null.");
        return commentRepository.findByPostIdAndReplierIsNull(id)
                .stream().map(this::convertOuter).toList();
    }

    @Override
    public List<CommentVO> replies(Long replier) {
        return commentRepository.findByReplier(replier)
                .stream().map(this::convertOuter).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentVO create(CommentDTO dto) {
        Comment comment = new Comment();
        BeanCopier copier = BeanCopier.create(CommentDTO.class, Comment.class, false);
        copier.copy(dto, comment, null);

        comment = commentRepository.saveAndFlush(comment);
        // 添加关联帖子的评论数
        this.postStatisticsRepository.increaseComment(comment.getPostId());
        return this.convertOuter(comment);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param comment 信息
     * @return 输出转换后的vo对象
     */
    private CommentVO convertOuter(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanCopier copier = BeanCopier.create(Comment.class, CommentVO.class, false);
        copier.copy(comment, vo, null);

        Long count = commentRepository.countByReplier(comment.getId());
        vo.setCount(count);
        return vo;
    }

}
