/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.bo.CategoryBO;
import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.dto.PostDTO;
import io.leafage.basic.assets.repository.CategoryRepository;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.service.PostsContentService;
import io.leafage.basic.assets.service.PostsService;
import io.leafage.basic.assets.vo.PostContentVO;
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
import top.leafage.common.basic.AbstractBasicService;
import top.leafage.common.basic.ValidMessage;

import javax.naming.NotContextException;

/**
 * posts service impl
 *
 * @author liwenqiang 2018-12-20 9:54
 **/
@Service
public class PostsServiceImpl extends AbstractBasicService implements PostsService {

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
    public Mono<Page<PostVO>> retrieve(int page, int size, String sort, String category) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, StringUtils.hasText(sort) ? sort : "modifyTime"));
        Flux<PostVO> voFlux;
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
    public Mono<PostVO> fetch(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new)) // 如果查询没有返回则抛出异常
                .flatMap(posts -> categoryRepository.findById(posts.getCategoryId()).map(category -> { // 查询关联分类信息
                            PostContentVO contentVO = new PostContentVO();
                            BeanUtils.copyProperties(posts, contentVO);
                            // 转换分类对象
                            CategoryBO categoryBO = new CategoryBO();
                            categoryBO.setCode(category.getCode());
                            categoryBO.setName(category.getName());
                            contentVO.setCategory(categoryBO);
                            return contentVO;
                        }).flatMap(postsContentVO -> postsContentService.fetchByPostsId(posts.getId()) // 查询帖子内容
                                .map(contentInfo -> {
                                    ContentBO contentVO = new ContentBO();
                                    contentVO.setContent(contentInfo.getContent());
                                    contentVO.setCatalog(contentInfo.getCatalog());
                                    postsContentVO.setContent(contentVO);
                                    return postsContentVO;
                                }).defaultIfEmpty(postsContentVO))
                );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> create(PostDTO postDTO) {
        return categoryRepository.getByCodeAndEnabledTrue(postDTO.getCategory().getCode())
                .switchIfEmpty(Mono.error(NotContextException::new))
                .map(category -> {
                    Posts posts = new Posts();
                    BeanUtils.copyProperties(postDTO, posts);
                    posts.setCode(this.generateCode());
                    posts.setCategoryId(category.getId());
                    return posts;
                }).flatMap(info -> postsRepository.insert(info).map(posts -> {
                            PostsContent postsContent = new PostsContent();
                            postsContent.setPostsId(posts.getId());
                            postsContent.setCatalog(postDTO.getContent().getCatalog());
                            postsContent.setContent(postDTO.getContent().getContent());
                            return postsContent;
                        }).flatMap(postsContentService::create).map(postsContent -> info)
                ).flatMap(this::convertOuter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PostVO> modify(String code, PostDTO postDTO) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code)
                .switchIfEmpty(Mono.error(NotContextException::new))
                .flatMap(info -> categoryRepository.getByCodeAndEnabledTrue(postDTO.getCategory().getCode())
                        .switchIfEmpty(Mono.error(NotContextException::new))
                        .map(category -> {
                            // 将信息复制到info
                            BeanUtils.copyProperties(postDTO, info);
                            info.setCategoryId(category.getId());
                            return info;
                        }).flatMap(postsInfo -> postsRepository.save(postsInfo).flatMap(posts ->
                                postsContentService.fetchByPostsId(posts.getId())
                                        .doOnNext(postsContent -> {
                                            postsContent.setCatalog(postDTO.getContent().getCatalog());
                                            postsContent.setContent(postDTO.getContent().getContent());
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
    public Mono<PostVO> next(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsRepository.findByIdGreaterThanAndEnabledTrue(posts.getId(),
                        PageRequest.of(0, 1, Sort.Direction.ASC, "id")).next()
        ).flatMap(this::convertOuter);
    }

    @Override
    public Mono<PostVO> previous(String code) {
        Assert.hasText(code, ValidMessage.CODE_NOT_BLANK);
        return postsRepository.getByCodeAndEnabledTrue(code).flatMap(posts ->
                postsRepository.findByIdLessThanAndEnabledTrue(posts.getId(),
                        PageRequest.of(0, 1, Sort.Direction.DESC, "id")).next()
        ).flatMap(this::convertOuter);
    }

    @Override
    public Flux<PostVO> search(String keyword) {
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
     * 对象转换为输出结果对象
     *
     * @param posts 信息
     * @return 输出转换后的vo对象
     */
    private Mono<PostVO> convertOuter(Posts posts) {
        return Mono.just(posts).map(p -> {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(p, postVO);
            return postVO;
        }).flatMap(postsVO -> category(posts.getCategoryId(), postsVO));
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
