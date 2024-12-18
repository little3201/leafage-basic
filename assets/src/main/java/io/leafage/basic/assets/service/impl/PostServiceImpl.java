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

import io.leafage.basic.assets.domain.Post;
import io.leafage.basic.assets.domain.PostContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostContentRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.service.PostService;
import io.leafage.basic.assets.vo.PostVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * post service impl
 *
 * @author wq li
 */
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final CategoryRepository categoryRepository;


    /**
     * <p>Constructor for PostServiceImpl.</p>
     *
     * @param postRepository        a {@link io.leafage.basic.assets.repository.PostRepository} object
     * @param postContentRepository a {@link io.leafage.basic.assets.repository.PostContentRepository} object
     * @param categoryRepository    a {@link io.leafage.basic.assets.repository.CategoryRepository} object
     */
    public PostServiceImpl(PostRepository postRepository, PostContentRepository postContentRepository,
                           CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.postContentRepository = postContentRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Page<PostVO>> retrieve(int page, int size, String sortBy, boolean descending) {
        Sort sort = Sort.by(descending ? Sort.Direction.DESC : Sort.Direction.ASC,
                StringUtils.hasText(sortBy) ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Flux<Post> postFlux = postRepository.findByEnabledTrue(pageable);
        Mono<Long> count = postRepository.count();

        return postFlux.flatMap(this::convert).collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageable, objects.getT2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<PostVO> fetch(Long id) {
        Assert.notNull(id, "id must not be null.");
        return postRepository.findById(id).flatMap(post ->
                categoryRepository.findById(post.getCategoryId()).map(category -> {
                    PostVO postVO = new PostVO();
                    BeanUtils.copyProperties(post, postVO);
                    postVO.setCategory(category.getName());
                    return postVO;
                }).flatMap(vo -> postContentRepository.getByPostId(post.getId()) // 查询帖子内容
                        .map(postsContent -> {
                            vo.setContext(postsContent.getContext());
                            return vo;
                        }).defaultIfEmpty(vo))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> create(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        return postRepository.save(post).flatMap(p -> {
            PostContent postContent = new PostContent();
            postContent.setPostId(p.getId());
            postContent.setContext(postDTO.getContext());
            return postContentRepository.save(postContent).flatMap(pc -> this.convert(post));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> modify(Long id, PostDTO postDTO) {
        Assert.notNull(id, "id must not be null.");
        return postRepository.findById(id).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .doOnNext(post -> BeanUtils.copyProperties(postDTO, post))
                .flatMap(postRepository::save)
                .flatMap(p -> postContentRepository.getByPostId(p.getId()).switchIfEmpty(Mono.just(new PostContent()))
                        .flatMap(postContent -> {
                            postContent.setPostId(p.getId());
                            postContent.setContext(postDTO.getContext());
                            return postContentRepository.save(postContent);
                        }).flatMap(pc -> this.convert(p))
                );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "id must not be null.");
        return postContentRepository.getByPostId(id).switchIfEmpty(Mono.error(NoSuchElementException::new))
                .flatMap(postContent -> {
                            if (Objects.nonNull(postContent.getId())) {
                                return postContentRepository.deleteById(postContent.getId());
                            }
                            return Mono.error(NoSuchElementException::new);
                        }
                )
                .then(postRepository.deleteById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<PostVO> search(String keyword) {
        Assert.hasText(keyword, "keyword must not be empty.");
        return postRepository.findAllByTitle(keyword).flatMap(this::convert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> exists(String title) {
        Assert.hasText(title, "title must not be empty.");
        return postRepository.existsByTitle(title);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param post 信息
     * @return 输出转换后的vo对象
     */
    private Mono<PostVO> convert(Post post) {
        return Mono.just(post).flatMap(p -> {
            PostVO vo = new PostVO();
            BeanUtils.copyProperties(p, vo);
            vo.setLastModifiedDate(p.getLastModifiedDate().orElse(null));
            // 查询关联分类信息
            return categoryRepository.findById(p.getCategoryId()).map(category -> {
                vo.setCategory(category.getName());
                return vo;
            });
        });
    }

}
