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
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Post;
import io.leafage.basic.assets.document.PostContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostRepository;
import io.leafage.basic.assets.service.PostContentService;
import io.leafage.basic.assets.service.PostService;
import io.leafage.basic.assets.vo.PostVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.AbstractBasicService;
import top.leafage.common.ValidMessage;

import javax.naming.NotContextException;

/**
 * posts service impl
 *
 * @author liwenqiang 2018-12-20 9:54
 **/
@Service
public class PostServiceImpl extends AbstractBasicService implements PostService {

    private final PostRepository postRepository;
    private final PostContentService postContentService;
    private final CategoryRepository categoryRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    public PostServiceImpl(PostRepository postRepository, PostContentService postContentService,
                           CategoryRepository categoryRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.postRepository = postRepository;
        this.postContentService = postContentService;
        this.categoryRepository = categoryRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Page<PostVO>> retrieve(int page, int size, String sort, String category) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, StringUtils.hasText(sort) ? sort : "modifyTime"));
        Flux<PostVO> voFlux;
        Mono<Long> count;

        if (StringUtils.hasText(category)) {
            Mono<Category> categoryMono = categoryRepository.getByCodeAndEnabledTrue(category);
            voFlux = categoryMono.flatMapMany(c -> postRepository.findByCategoryIdAndEnabledTrue(c.getId(), pageRequest)
                    .flatMap(this::convertOuter));
            count = categoryMono.flatMap(c -> postRepository.countByCategoryIdAndEnabledTrue(c.getId()));
        } else {
            voFlux = postRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);
            count = postRepository.countByEnabledTrue();
        }

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<PostVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new)) // 如果查询没有返回则抛出异常
                .flatMap(posts -> categoryRepository.findById(posts.getCategoryId()).map(category -> { // 查询关联分类信息
                            PostVO contentVO = new PostVO();
                            BeanUtils.copyProperties(posts, contentVO);
                            // 转换分类对象
                            CategoryBO categoryBO = new CategoryBO();
                            categoryBO.setCode(category.getCode());
                            categoryBO.setName(category.getName());
                            contentVO.setCategory(categoryBO);
                            return contentVO;
                        }).flatMap(postVO -> postContentService.fetchByPostsId(posts.getId()) // 查询帖子内容
                                .map(postsContent -> {
                                    ContentBO contentVO = new ContentBO();
                                    contentVO.setContent(postsContent.getContent());
                                    contentVO.setCatalog(postsContent.getCatalog());
                                    postVO.setContent(contentVO);
                                    return postVO;
                                }).defaultIfEmpty(postVO))
                );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> create(PostDTO postDTO) {
        return categoryRepository.getByCodeAndEnabledTrue(postDTO.getCategory().getCode())
                .switchIfEmpty(Mono.error(NotContextException::new))
                .map(category -> {
                    Post post = new Post();
                    BeanUtils.copyProperties(postDTO, post);
                    post.setCode(this.generateCode());
                    post.setCategoryId(category.getId());
                    return post;
                }).flatMap(info -> postRepository.insert(info).map(posts -> {
                            PostContent postContent = new PostContent();
                            postContent.setPostsId(posts.getId());
                            postContent.setCatalog(postDTO.getContent().getCatalog());
                            postContent.setContent(postDTO.getContent().getContent());
                            return postContent;
                        }).flatMap(postContentService::create).map(postsContent -> info)
                ).flatMap(this::convertOuter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> modify(String code, PostDTO postDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(info -> categoryRepository.getByCodeAndEnabledTrue(postDTO.getCategory().getCode())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .map(category -> {
                            // 将信息复制到info
                            BeanUtils.copyProperties(postDTO, info);
                            info.setCategoryId(category.getId());
                            return info;
                        }).flatMap(postsInfo -> postRepository.save(postsInfo).flatMap(posts ->
                                postContentService.fetchByPostsId(posts.getId())
                                        .doOnNext(postsContent -> {
                                            postsContent.setCatalog(postDTO.getContent().getCatalog());
                                            postsContent.setContent(postDTO.getContent().getContent());
                                        })
                                        .flatMap(postsContent -> postContentService.modify(posts.getId(), postsContent))
                                        .map(postsContent -> postsInfo)).flatMap(this::convertOuter))
                );
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(posts.getId())),
                        Update.update("enabled", false), Post.class).then()
        );
    }

    @Override
    public Mono<PostVO> next(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postRepository.findFirstByIdGreaterThanAndEnabledTrue(posts.getId())
        ).flatMap(this::convertOuter);
    }

    @Override
    public Mono<PostVO> previous(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postRepository.findByIdLessThanAndEnabledTrue(posts.getId(),
                        PageRequest.of(0, 1, Sort.Direction.DESC, "id")).next()
        ).flatMap(this::convertOuter);
    }

    @Override
    public Flux<PostVO> search(String keyword) {
        Assert.hasText(keyword, "keyword must not be blank.");
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(keyword);
        return postRepository.findAllBy(keyword, criteria).flatMap(this::convertOuter);
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
        }).flatMap(postsVO -> category(post.getCategoryId(), postsVO));
    }

    /**
     * 转换category
     *
     * @param categoryId category主键
     * @param vo         vo
     * @return 设置后的vo
     */
    private Mono<PostVO> category(ObjectId categoryId, PostVO vo) {
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
