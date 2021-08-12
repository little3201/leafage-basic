/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.leafage.basic.assets.document.Category;
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
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.leafage.common.basic.AbstractBasicService;
import javax.naming.NotContextException;

/**
 * 帖子信息 service 接口实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

    private static final String MESSAGE = "code is blank.";

    private final PostsRepository postsRepository;
    private final PostsContentService postsContentService;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final CategoryRepository categoryRepository;

    public PostsServiceImpl(PostsRepository postsRepository, PostsContentService postsContentService, ReactiveMongoTemplate reactiveMongoTemplate, CategoryRepository categoryRepository) {
        this.postsRepository = postsRepository;
        this.postsContentService = postsContentService;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Flux<PostsVO> retrieve() {
        return postsRepository.findByEnabledTrue().flatMap(this::convertOuter);
    }

    @Override
    public Flux<PostsVO> retrieve(int page, int size, String order) {
        Sort sort = Sort.by(Sort.Direction.DESC, StringUtils.hasText(order) ? order : "modifyTime");
        return postsRepository.findByEnabledTrue(PageRequest.of(page, size, sort))
                .flatMap(this::convertOuter);
    }

    @Override
    public Flux<PostsVO> retrieve(int page, int size, String category, String order) {
        Sort sort = Sort.by(Sort.Direction.DESC, StringUtils.hasText(order) ? order : "modifyTime");
        return categoryRepository.getByCodeAndEnabledTrue(category).flatMapMany(c ->
                postsRepository.findByCategoryIdAndEnabledTrue(c.getId(), PageRequest.of(page, size, sort))
                        .flatMap(this::convertOuter));
    }

    @Override
    public Mono<PostsContentVO> details(String code) {
        Assert.hasText(code, MESSAGE);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .map(posts -> {
                    // 更新viewed
                    this.incrementViewed(posts.getId()).subscribe();
                    return posts;
                })
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
    public Mono<Long> count() {
        return postsRepository.count();
    }

    @Override
    public Mono<PostsVO> create(PostsDTO postsDTO) {
        Mono<Posts> postsMono = categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory())
                .switchIfEmpty(Mono.error(NotContextException::new))
                .map(category -> {
                    Posts info = new Posts();
                    BeanUtils.copyProperties(postsDTO, info);
                    info.setCode(this.generateCode());
                    info.setCategoryId(category.getId());
                    return info;
                }).flatMap(info -> postsRepository.insert(info).switchIfEmpty(Mono.error(RuntimeException::new))
                        .doOnSuccess(posts -> {
                            // 添加内容信息
                            PostsContent postsContent = new PostsContent();
                            BeanUtils.copyProperties(postsDTO, postsContent);
                            postsContent.setPostsId(posts.getId());
                            // 调用subscribe()方法，消费create订阅
                            postsContentService.create(postsContent).subscribe();
                        }));
        return postsMono.flatMap(this::convertOuter);
    }

    @Override
    public Mono<PostsVO> modify(String code, PostsDTO postsDTO) {
        Assert.hasText(code, MESSAGE);
        Mono<Posts> postsMono = postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(info -> categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .map(category -> {
                            // 将信息复制到info
                            BeanUtils.copyProperties(postsDTO, info);
                            info.setCategoryId(category.getId());
                            return info;
                        }).flatMap(postsInfo -> postsRepository.save(postsInfo)
                                .switchIfEmpty(Mono.error(RuntimeException::new))
                                .doOnSuccess(posts ->
                                        // 更新成功后，将内容信息更新
                                        postsContentService.fetchByPostsId(posts.getId()).subscribe(detailsInfo -> {
                                            BeanUtils.copyProperties(postsDTO, detailsInfo);
                                            // 调用subscribe()方法，消费modify订阅
                                            postsContentService.modify(posts.getId(), detailsInfo).subscribe();
                                        })
                                ))
                );
        return postsMono.flatMap(this::convertOuter);
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
    public Mono<Boolean> exists(String title) {
        Assert.hasText(title, "title is blank.");
        return postsRepository.existsByTitle(title);
    }

    /**
     * viewed 原子更新（自增 1）
     *
     * @param id 主键
     * @return UpdateResult
     */
    private Mono<UpdateResult> incrementViewed(ObjectId id) {
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
        Mono<PostsVO> voMono = Mono.just(posts).map(p -> {
            PostsVO postsVO = new PostsVO();
            BeanUtils.copyProperties(p, postsVO);
            return postsVO;
        });
        Mono<Category> categoryMono = categoryRepository.findById(posts.getCategoryId());
        return voMono.zipWith(categoryMono, (vo, category) -> {
            vo.setCategory(category.getAlias());
            return vo;
        });
    }

}
