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
import io.leafage.basic.assets.vo.CategoryVO;
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
    public Mono<PostsVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new)) // 如果查询没有返回则抛出异常
                .flatMap(posts -> this.increaseViewed(posts.getId()).map(viewed -> { // 浏览量+1
                    posts.setViewed(viewed);
                    return posts;
                }).flatMap(p -> statisticsService.increase(LocalDate.now(), StatisticsFieldEnum.VIEWED).map(v -> p)))
                .flatMap(posts -> categoryRepository.findById(posts.getCategoryId()).map(category -> { // 查询关联分类信息
                            PostsContentVO extendVO = new PostsContentVO();
                            BeanUtils.copyProperties(posts, extendVO);
                            // 转换分类对象
                            CategoryVO basicVO = new CategoryVO();
                            BeanUtils.copyProperties(category, basicVO);
                            extendVO.setCategory(basicVO);
                            return extendVO;
                        }).flatMap(extendVO -> postsContentService.fetchByPostsId(posts.getId()) // 查询帖子内容
                                .map(contentInfo -> {
                                    ContentVO contentVO = new ContentVO();
                                    contentVO.setContent(contentInfo.getContent());
                                    contentVO.setCatalog(contentInfo.getCatalog());
                                    extendVO.setContent(contentVO);
                                    return extendVO;
                                }).defaultIfEmpty(extendVO))
                );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostsVO> create(PostsDTO postsDTO) {
        return categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory().getCode())
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
                .flatMap(info -> categoryRepository.getByCodeAndEnabledTrue(postsDTO.getCategory().getCode())
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
        }).flatMap(postsVO -> category(posts.getCategoryId(), postsVO));
    }

    /**
     * 转换category
     *
     * @param categoryId category主键
     * @param vo         vo
     * @return 设置后的vo
     */
    private Mono<PostsVO> category(ObjectId categoryId, PostsVO vo) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    // 转换分类对象
                    CategoryVO basicVO = new CategoryVO();
                    BeanUtils.copyProperties(category, basicVO);
                    vo.setCategory(basicVO);
                    return vo;
                }).switchIfEmpty(Mono.just(vo));
    }
}
