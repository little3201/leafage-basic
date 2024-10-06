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
import io.leafage.basic.assets.service.CommentService;
import io.leafage.basic.assets.vo.CommentVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * comment service impl.
 *
 * @author wq li
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    /**
     * <p>Constructor for CommentServiceImpl.</p>
     *
     * @param commentRepository a {@link io.leafage.basic.assets.repository.CommentRepository} object
     */
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CommentVO> retrieve(int page, int size, String sortBy, boolean descending) {
        Pageable pageable = pageable(page, size, sortBy, descending);
        return commentRepository.findAll(pageable).map(this::convert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommentVO> relation(Long id) {
        Assert.notNull(id, "id must not be null.");
        return commentRepository.findAllByPostIdAndReplierIsNull(id)
                .stream().map(this::convert).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommentVO> replies(Long replier) {
        return commentRepository.findAllByReplier(replier)
                .stream().map(this::convert).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentVO create(CommentDTO dto) {
        Comment comment = new Comment();
        BeanCopier copier = BeanCopier.create(CommentDTO.class, Comment.class, false);
        copier.copy(dto, comment, null);

        comment = commentRepository.saveAndFlush(comment);
        return this.convert(comment);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param comment 信息
     * @return 输出转换后的vo对象
     */
    private CommentVO convert(Comment comment) {
        CommentVO vo = new CommentVO();
        BeanCopier copier = BeanCopier.create(Comment.class, CommentVO.class, false);
        copier.copy(comment, vo, null);

        // get last modified date
        Optional<Instant> optionalInstant = comment.getLastModifiedDate();
        optionalInstant.ifPresent(vo::setLastModifiedDate);

        Long count = commentRepository.countByReplier(comment.getId());
        vo.setCount(count);
        return vo;
    }

}
