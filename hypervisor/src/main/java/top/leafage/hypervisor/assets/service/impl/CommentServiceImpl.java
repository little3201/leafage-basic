/*
 *  Copyright 2018-2025 little3201.
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

package top.leafage.hypervisor.assets.service.impl;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.hypervisor.assets.domain.Comment;
import top.leafage.hypervisor.assets.domain.dto.CommentDTO;
import top.leafage.hypervisor.assets.domain.vo.CommentVO;
import top.leafage.hypervisor.assets.repository.CommentRepository;
import top.leafage.hypervisor.assets.service.CommentService;

import java.util.NoSuchElementException;


/**
 * comment service impl
 *
 * @author wq li
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final BeanCopier copier = BeanCopier.create(CommentDTO.class, Comment.class, false);
    private final CommentRepository commentRepository;

    /**
     * Constructor for CommentServiceImpl.
     *
     * @param commentRepository a {@link CommentRepository} object
     */
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<CommentVO> comments(Long postId) {
        Assert.notNull(postId, String.format(_MUST_NOT_BE_NULL, "postId"));

        return commentRepository.findByPostIdAndReplierIsNull(postId)
                .map(CommentVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<CommentVO> replies(Long replier) {
        Assert.notNull(replier, String.format(_MUST_NOT_BE_NULL, "replier"));

        return commentRepository.findByReplier(replier)
                .map(CommentVO::from);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<CommentVO> create(CommentDTO dto) {
        return commentRepository.save(CommentDTO.toEntity(dto))
                .map(CommentVO::from);
    }

    @Override
    public Mono<CommentVO> modify(Long id, CommentDTO dto) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(existing -> {
                    copier.copy(dto, existing, null);
                    return commentRepository.save(existing);
                })
                .map(CommentVO::from);
    }

    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        return commentRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NoSuchElementException("comment not found: " + id));
                    }
                    return commentRepository.deleteById(id);
                });
    }
}
