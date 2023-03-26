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

import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.bo.ContentBO;
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
 * posts service impl
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
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, StringUtils.hasText(sort) ? sort : "modifyTime"));
        Flux<Post> postFlux;
        Mono<Long> count;
        // if category not null
        if (null != categoryId) {
            postFlux = postRepository.findByCategoryId(categoryId, pageRequest);
            // count filter by category
            count = postRepository.countByCategoryId(categoryId);
        } else {
            postFlux = postRepository.findAll(pageRequest);
            // count all
            count = postRepository.count();
        }

        return postFlux.flatMap(this::convertOuter).collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<PostVO> fetch(Long id) {
        Assert.notNull(id, "id must not be null.");
        return postRepository.findById(id).flatMap(posts ->
                // 查询关联分类信息
                categoryRepository.findById(posts.getCategoryId()).map(category -> {
                    PostVO contentVO = new PostVO();
                    BeanUtils.copyProperties(posts, contentVO);
                    // 转换分类对象
                    CategoryBO categoryBO = new CategoryBO();
                    categoryBO.setName(category.getName());
                    contentVO.setCategory(categoryBO);
                    return contentVO;
                }).flatMap(postVO -> postContentRepository.getByPostId(posts.getId()) // 查询帖子内容
                        .map(postsContent -> {
                            ContentBO contentVO = new ContentBO();
                            contentVO.setContext(postsContent.getContext());
                            contentVO.setCatalog(postsContent.getCatalog());
                            postVO.setContent(contentVO);
                            return postVO;
                        }).defaultIfEmpty(postVO))
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> create(PostDTO postDTO) {
        return Mono.just(postDTO).map(dto -> {
            Post post = new Post();
            BeanUtils.copyProperties(postDTO, post);
            post.setCategoryId(dto.getCategory().getId());
            return post;
        }).flatMap(info -> postRepository.save(info).map(posts -> {
                    PostContent postContent = new PostContent();
                    postContent.setPostId(posts.getId());
                    postContent.setCatalog(postDTO.getContent().getCatalog());
                    postContent.setContext(postDTO.getContent().getContext());
                    return postContent;
                }).flatMap(postContentRepository::save).map(postsContent -> info)
        ).flatMap(this::convertOuter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> modify(Long id, PostDTO postDTO) {
        Assert.notNull(id, "id must not be null.");
        return postRepository.findById(id).flatMap(post ->
                Mono.just(postDTO).map(dto -> {
                    // 将信息复制到info
                    BeanUtils.copyProperties(postDTO, post);
                    post.setCategoryId(dto.getCategory().getId());
                    return post;
                }).flatMap(postsInfo -> postRepository.save(postsInfo).flatMap(posts ->
                        postContentRepository.getByPostId(posts.getId())
                                .doOnNext(postsContent -> {
                                    postsContent.setCatalog(postDTO.getContent().getCatalog());
                                    postsContent.setContext(postDTO.getContent().getContext());
                                })
                                .flatMap(postContentRepository::save)
                                .map(postsContent -> postsInfo)).flatMap(this::convertOuter))
        );
    }

    @Override
    public Mono<Void> remove(Long id) {
        Assert.notNull(id, "id must not be null.");
        return postRepository.deleteById(id);
    }

    @Override
    public Flux<PostVO> search(String keyword) {
        Assert.hasText(keyword, "keyword must not be blank.");
        return postRepository.findAllByTitle(keyword).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Boolean> exist(String title) {
        Assert.hasText(title, "title must not be blank.");
        return postRepository.existsByTitle(title);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param post 信息
     * @return 输出转换后的vo对象
     */
    private Mono<PostVO> convertOuter(Post post) {
        return Mono.just(post).map(p -> {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(p, postVO);
            return postVO;
        }).flatMap(vo -> category(post.getCategoryId(), vo));
    }

    /**
     * 转换category
     *
     * @param categoryId category主键
     * @param vo         vo
     * @return 设置后的vo
     */
    private Mono<PostVO> category(Long categoryId, PostVO vo) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    // 转换分类对象
                    CategoryBO basicVO = new CategoryBO();
                    BeanUtils.copyProperties(category, basicVO);
                    vo.setCategory(basicVO);
                    return vo;
                }).switchIfEmpty(Mono.just(vo));
    }
}
