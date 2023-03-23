/*
 *  Copyright 2018-2023 the original author or authors.
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.AbstractBasicService;
import top.leafage.common.ValidMessage;

import javax.naming.NotContextException;
import java.util.NoSuchElementException;

/**
 * comment service impl
 *
 * @author liwenqiang 2018-12-06 22:09
 **/
@Service
public class CommentServiceImpl extends AbstractBasicService implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Mono<Page<CommentVO>> retrieve(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifyTime"));
        Flux<CommentVO> voFlux = commentRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);

        Mono<Long> count = commentRepository.countByEnabledTrue();

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Flux<CommentVO> relation(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postRepository.getByCodeAndEnabledTrue(code).flatMapMany(posts ->
                commentRepository.findByPostIdAndReplierIsNullAndEnabledTrue(posts.getId()).flatMap(this::convertOuter));
    }

    @Override
    public Flux<CommentVO> replies(String replier) {
        Assert.hasText(replier, "replier must not be blank.");
        return commentRepository.findByReplierAndEnabledTrue(replier).flatMap(this::convertOuter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<CommentVO> create(CommentDTO commentDTO) {
        return postRepository.getByCodeAndEnabledTrue(commentDTO.getPosts()).map(posts -> {
                    Comment comment = new Comment();
                    BeanUtils.copyProperties(commentDTO, comment);
                    comment.setCode(this.generateCode());
                    comment.setPostId(posts.getId());
                    return comment;
                }).switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(commentRepository::save)
                .flatMap(this::convertOuter);
    }

    @Override
    public Mono<CommentVO> modify(String code, CommentDTO commentDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return commentRepository.getByCodeAndEnabledTrue(code).doOnNext(comment ->
                        BeanUtils.copyProperties(commentDTO, comment)).switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(commentRepository::save).flatMap(this::convertOuter);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param comment 信息
     * @return 输出转换后的vo对象
     */
    private Mono<CommentVO> convertOuter(Comment comment) {
        return Mono.just(comment).map(c -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(c, commentVO);
            return commentVO;
        }).flatMap(commentVO -> postRepository.findById(comment.getPostId())
                .switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(posts -> commentVO.setPosts(posts.getCode()))
                .flatMap(vo -> commentRepository.countByReplierAndEnabledTrue(comment.getCode())
                        .switchIfEmpty(Mono.just(0L)).map(count -> {
                            commentVO.setCount(count);
                            return commentVO;
                        })));
    }

}
