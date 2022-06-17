/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.constants.StatisticsFieldEnum;
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.dto.PostsDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.PostsContentService;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.service.StatisticsService;
import io.leafage.basic.assets.vo.ContentVO;
import io.leafage.basic.assets.vo.PostsContentVO;
import io.leafage.basic.assets.vo.PostsVO;
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
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.ValidMessage;
import javax.naming.NotContextException;
import java.time.LocalDate;

/**
 * posts service impl
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

    private final PostsRepository postsRepository;
    private final PostsContentService postsContentService;
    private final CategoryRepository categoryRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final StatisticsService statisticsService;

    public PostsServiceImpl(PostsRepository postsRepository, PostsContentService postsContentService,
                            CategoryRepository categoryRepository, ReactiveMongoTemplate reactiveMongoTemplate,
                            StatisticsService statisticsService) {
        this.postsRepository = postsRepository;
        this.postsContentService = postsContentService;
        this.categoryRepository = categoryRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.statisticsService = statisticsService;
    }

    @Override
    public Mono<Page<PostsVO>> retrieve(int page, int size, String sort, String category) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, StringUtils.hasText(sort) ? sort : "modifyTime"));
        Flux<PostsVO> voFlux;
        Mono<Long> count;

        if (StringUtils.hasText(category)) {
            Mono<Category> categoryMono = categoryRepository.getByCodeAndEnabledTrue(category);
            voFlux = categoryMono.flatMapMany(c -> postsRepository.findByCategoryIdAndEnabledTrue(c.getId(), pageRequest)
                    .flatMap(this::convertOuter));
            count = categoryMono.flatMap(c -> postsRepository.countByCategoryIdAndEnabledTrue(c.getId()));
        } else {
            voFlux = postsRepository.findByEnabledTrue(pageRequest).flatMap(this::convertOuter);
            count = postsRepository.countByEnabledTrue();
        }

        return voFlux.collectList().zipWith(count).map(objects ->
                new PageImpl<>(objects.getT1(), pageRequest, objects.getT2()));
    }

    @Override
    public Mono<PostsContentVO> details(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(posts -> this.increaseViewed(posts.getId()).map(viewed -> {
                            posts.setViewed(viewed);
                            return posts;
                        })
                        .flatMap(p -> statisticsService.increase(LocalDate.now(), StatisticsFieldEnum.VIEWED).map(v -> p)))
                .flatMap(posts -> categoryRepository.findById(posts.getCategoryId()).map(category -> {
                            PostsContentVO pcv = new PostsContentVO();
                            BeanUtils.copyProperties(posts, pcv);
                            pcv.setCategory(category.getName());
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
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
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
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsContentService.fetchByPostsId(posts.getId()).map(postsContent -> {
                    ContentVO contentVO = new ContentVO();
                    BeanUtils.copyProperties(postsContent, contentVO);
                    return contentVO;
                })
        );
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
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
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
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(posts.getId())),
                        Update.update("enabled", false), Posts.class).then()
        );
    }

    @Override
    public Mono<PostsVO> next(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsRepository.findByIdGreaterThanAndEnabledTrue(posts.getId(),
                        PageRequest.of(0, 1, Sort.Direction.ASC, "id")).next()
        ).flatMap(this::convertOuter);
    }

    @Override
    public Mono<PostsVO> previous(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsRepository.findByIdLessThanAndEnabledTrue(posts.getId(),
                        PageRequest.of(0, 1, Sort.Direction.DESC, "id")).next()
        ).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Integer> like(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where("code").is(code)),
                        new Update().inc("likes", 1), Posts.class)
                .flatMap(updateResult -> statisticsService.increase(LocalDate.now(), StatisticsFieldEnum.LIKES))
                .flatMap(updateResult -> postsRepository.getByCodeAndEnabledTrue(code).map(Posts::getLikes));
    }

    @Override
    public Flux<PostsVO> search(String keyword) {
        Assert.hasText(keyword, "keyword must not be blank.");
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(keyword);
        return postsRepository.findAllBy(keyword, criteria).flatMap(this::convertOuter);
    }

    @Override
    public Mono<Boolean> exist(String title) {
        Assert.hasText(title, "title must not be blank.");
        return postsRepository.existsByTitle(title);
    }

    /**
     * viewed 原子更新（自增 1）
     *
     * @param id 主键
     * @return UpdateResult
     */
    private Mono<Integer> increaseViewed(ObjectId id) {
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where("id").is(id)),
                new Update().inc("viewed", 1), Posts.class).flatMap(updateResult ->
                postsRepository.findById(id).map(Posts::getViewed));
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
                    postsVO.setCategory(category.getName());
                    return postsVO;
                }).switchIfEmpty(Mono.just(postsVO)));
    }
}
