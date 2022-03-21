/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.PostsContentService;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;

import javax.naming.NotContextException;

/**
 * posts service impl
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

    private static final String MESSAGE = "code is blank.";

    private final PostsRepository postsRepository;
    private final PostsContentService postsContentService;
    private final CategoryRepository categoryRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public PostsServiceImpl(PostsRepository postsRepository, PostsContentService postsContentService,
                            CategoryRepository categoryRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.postsRepository = postsRepository;
        this.postsContentService = postsContentService;
        this.categoryRepository = categoryRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<PostsVO> retrieve() {
        return postsRepository.findByEnabledTrue().flatMap(this::convertOuter);
    }

    @Override
    public Flux<PostsVO> retrieve(int page, int size, String sort) {
        Sort s = Sort.by(Sort.Direction.DESC, StringUtils.hasText(sort) ? sort : "modifyTime");
        return postsRepository.findByEnabledTrue(PageRequest.of(page, size, s)).flatMap(this::convertOuter);
    }

    @Override
    public Flux<PostsVO> retrieve(int page, int size, String sort, String category) {
        Sort s = Sort.by(Sort.Direction.DESC, StringUtils.hasText(sort) ? sort : "modifyTime");
        return categoryRepository.getByCodeAndEnabledTrue(category).flatMapMany(c ->
                postsRepository.findByCategoryIdAndEnabledTrue(c.getId(), PageRequest.of(page, size, s))
                        .flatMap(this::convertOuter));
    }

    @Override
    public Mono<PostsContentVO> details(String code) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(posts -> this.increaseViewed(posts.getId()).map(updateResult -> {
                    if (updateResult.wasAcknowledged()) {
                        posts.setViewed(posts.getViewed() + 1);
                    }
                    return posts;
                }))
                .flatMap(posts -> categoryRepository.findById(posts.getCategoryId()).map(category -> {
                            PostsContentVO pcv = new PostsContentVO();
                            BeanUtils.copyProperties(posts, pcv);
                            pcv.setCategory(category.getAlias());
                            return pcv;
                        }).flatMap(pcv -> postsContentService.fetchByPostsId(posts.getId()).map(contentInfo -> {
                            pcv.setContent(contentInfo.getContent());
                            pcv.setCatalog(contentInfo.getCatalog());
                            return pcv;
                        }).defaultIfEmpty(pcv))
                );
    }

    @Override
    public Mono<PostsVO> fetch(String code) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .flatMap(posts -> categoryRepository.findById(posts.getCategoryId()).map(category -> {
                            PostsVO pcv = new PostsVO();
                            BeanUtils.copyProperties(posts, pcv);
                            pcv.setViewed(posts.getViewed() + 1);
                            pcv.setCategory(category.getCode());
                            return pcv;
                        })
                );
    }

    @Override
    public Mono<ContentVO> content(String code) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsContentService.fetchByPostsId(posts.getId()).map(postsContent -> {
                    ContentVO contentVO = new ContentVO();
                    BeanUtils.copyProperties(postsContent, contentVO);
                    return contentVO;
                })
        );
    }

    @Override
    public Mono<Long> count(String category) {
        if (StringUtils.hasText(category)) {
            return categoryRepository.getByCodeAndEnabledTrue(category).flatMap(ca ->
                    postsRepository.countByCategoryIdAndEnabledTrue(ca.getId()));
        }
        return postsRepository.count();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostsVO> create(PostsDTO postsDTO) {
        return categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory())
                .switchIfEmpty(Mono.error(NotContextException::new))
                .map(category -> {
                    Posts info = new Posts();
                    BeanUtils.copyProperties(postsDTO, info);
                    info.setCode(this.generateCode());
                    info.setCategoryId(category.getId());
                    return info;
                }).flatMap(info -> postsRepository.insert(info).map(posts -> {
                            PostsContent postsContent = new PostsContent();
                            postsContent.setPostsId(posts.getId());
                            postsContent.setCatalog(postsDTO.getCatalog());
                            postsContent.setContent(postsDTO.getContent());
                            return postsContent;
                        }).flatMap(postsContentService::create).map(postsContent -> info)
                ).flatMap(this::convertOuter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostsVO> modify(String code, PostsDTO postsDTO) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(info -> categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .map(category -> {
                            // 将信息复制到info
                            BeanUtils.copyProperties(postsDTO, info);
                            info.setCategoryId(category.getId());
                            return info;
                        }).flatMap(postsInfo -> postsRepository.save(postsInfo).flatMap(posts ->
                                postsContentService.fetchByPostsId(posts.getId())
                                        .doOnNext(postsContent -> {
                                            postsContent.setCatalog(postsDTO.getCatalog());
                                            postsContent.setContent(postsDTO.getContent());
                                        })
                                        .flatMap(postsContent -> postsContentService.modify(posts.getId(), postsContent))
                                        .map(postsContent -> postsInfo)).flatMap(this::convertOuter))
                );
    }

    @Override
    public Mono<Void> remove(String code) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(posts.getId())),
                        Update.update("enabled", false), Posts.class).then()
        );
    }

    @Override
    public Mono<PostsVO> next(String code) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsRepository.findByIdGreaterThanAndEnabledTrue(posts.getId(),
                        PageRequest.of(0, 1, Sort.Direction.ASC, "id")).next()
        ).flatMap(this::convertOuter);
    }

    @Override
    public Mono<PostsVO> previous(String code) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsRepository.findByIdLessThanAndEnabledTrue(posts.getId(),
                        PageRequest.of(0, 1, Sort.Direction.DESC, "id")).next()
        ).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Integer> like(String code) {
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where("code").is(code)),
                new Update().inc("likes", 1), Posts.class).flatMap(updateResult ->
                postsRepository.getByCodeAndEnabledTrue(code).map(Posts::getLikes));
    }

    @Override
    public Flux<PostsVO> search(String keyword) {
        return postsRepository.findByTitleIgnoreCaseLikeAndEnabledTrue(keyword).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Boolean> exist(String title) {
        Assert.hasText(title, "title is blank.");
        return postsRepository.existsByTitle(title);
    }

    /**
     * viewed 原子更新（自增 1）
     *
     * @param id 主键
     * @return UpdateResult
     */
    private Mono<UpdateResult> increaseViewed(ObjectId id) {
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(id)),
                new Update().inc("viewed", 1), Posts.class);
    }

    /**
     * 对象转换为输出结果对象
     *
     * @param posts 信息
     * @return 输出转换后的vo对象
     */
    private Mono<PostsVO> convertOuter(Posts posts) {
        return Mono.just(posts).map(p -> {
            PostsVO postsVO = new PostsVO();
            BeanUtils.copyProperties(p, postsVO);
            return postsVO;
        }).flatMap(postsVO -> categoryRepository.findById(posts.getCategoryId())
                .map(category -> {
                    postsVO.setCategory(category.getAlias());
                    return postsVO;
                }).switchIfEmpty(Mono.just(postsVO)));
    }
}
