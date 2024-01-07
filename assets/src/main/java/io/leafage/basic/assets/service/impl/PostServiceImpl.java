/*
 *  Copyright 2018-2024 the original author or authors.
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * post service impl
 *
 * @author liwenqiang 2018-12-20 9:54
 **/
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final CategoryRepository categoryRepository;


    public PostServiceImpl(PostRepository postRepository, PostContentRepository postContentRepository,
                           CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.postContentRepository = postContentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Mono<Page<PostVO>> retrieve(int page, int size, String sort, Long categoryId) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,
                StringUtils.hasText(sort) ? sort : "modifyTime"));
        Flux<Post> postFlux;
        Mono<Long> count;
        // if categoryId null, select all, else filter by categoryId
        if (null == categoryId) {
            postFlux = postRepository.findByEnabledTrue(pageRequest);
            count = postRepository.count();
        } else {
            postFlux = postRepository.findByCategoryId(categoryId, pageRequest);
            count = postRepository.countByCategoryId(categoryId);
        }

        return postFlux.flatMap(this::convertOuter).collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<PostVO> fetch(Long id) {
        Assert.notNull(id, "id must not be null.");
        return postRepository.findById(id).flatMap(post ->
                // 查询关联分类信息
                categoryRepository.findById(post.getCategoryId()).map(category -> {
                    PostVO postVO = new PostVO();
                    BeanUtils.copyProperties(post, postVO);
                    postVO.setCategory(category.getCategoryName());
                    return postVO;
                }).flatMap(vo -> postContentRepository.getByPostId(post.getId()) // 查询帖子内容
                        .map(postsContent -> {
                            vo.setContext(postsContent.getContext());
                            return vo;
                        }).defaultIfEmpty(vo))
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> create(PostDTO postDTO) {
        return Mono.just(postDTO).map(dto -> {
            Post post = new Post();
            BeanUtils.copyProperties(postDTO, post);
            post.setCategoryId(dto.getCategoryId());
            post.setOwner("admin");
            return post;
        }).flatMap(postRepository::save).flatMap(post -> {
            PostContent postContent = new PostContent();
            postContent.setPostId(post.getId());
            postContent.setContext(postDTO.getContext());
            return postContentRepository.save(postContent).flatMap(pc -> this.convertOuter(post));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> modify(Long id, PostDTO postDTO) {
        Assert.notNull(id, "id cannot be null.");
        return postRepository.findById(id).flatMap(post -> Mono.just(postDTO).map(dto -> {
                    // 将信息复制到info
                    BeanUtils.copyProperties(postDTO, post);
                    return post;
                }).flatMap(entity -> postRepository.save(entity)
                        .flatMap(p -> postContentRepository.getByPostId(p.getId())
                                .flatMap(postContent -> {
                                    postContent.setContext(postDTO.getContext());
                                    return postContentRepository.save(postContent).flatMap(pc -> this.convertOuter(p));
                                })
                        ))
        );
    }

    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "id cannot be null.");
        return postContentRepository.getByPostId(id).flatMap(postContent ->
                        postContentRepository.deleteById(postContent.getId()))
                .then(Mono.defer(() -> postRepository.deleteById(id)));
    }

    @Override
    public Flux<PostVO> search(String keyword) {
        Assert.hasText(keyword, "keyword cannot be blank.");
        return postRepository.findAllByTitle(keyword).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Boolean> exist(String title) {
        Assert.hasText(title, "title cannot be blank.");
        return postRepository.existsByTitle(title);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param post 信息
     * @return 输出转换后的vo对象
     */
    private Mono<PostVO> convertOuter(Post post) {
        return Mono.just(post).flatMap(p -> {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(p, postVO);
            // 查询关联分类信息
            return categoryRepository.findById(post.getCategoryId()).map(category -> {
                postVO.setCategory(category.getCategoryName());
                return postVO;
            });
        });
    }

}
