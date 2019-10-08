/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.enter.ArticleEnter;
import top.abeille.basic.assets.vo.outer.ArticleOuter;

import java.util.Objects;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class ArticleInfoServiceImpl implements ArticleInfoService {

    private final ArticleInfoRepository articleInfoRepository;

    public ArticleInfoServiceImpl(ArticleInfoRepository articleInfoRepository) {
        this.articleInfoRepository = articleInfoRepository;
    }

    @Override
    public Flux<ArticleOuter> findAll() {
        return articleInfoRepository.findAll().map(this::convertOuter);
    }

    @Override
    public Mono<ArticleOuter> getById(Long articleId) {
        return fetchByArticleId(articleId).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleOuter> save(Long articleId, ArticleEnter enter) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(enter, info);
        if (Objects.nonNull(articleId)) {
            return fetchByArticleId(articleId).flatMap(articleInfo -> articleInfoRepository.save(info).map(this::convertOuter));
        }
        return articleInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<Void> removeById(Long articleId) {
        ArticleInfo info = new ArticleInfo();
        info.setArticleId(articleId);
        return articleInfoRepository.findOne(Example.of(info)).flatMap(article -> articleInfoRepository.deleteById(article.getId()));
    }

    private Mono<ArticleInfo> fetchByArticleId(Long articleId) {
        if (Objects.isNull(articleId)) {
            return Mono.empty();
        }
        ArticleInfo info = new ArticleInfo();
        info.setArticleId(articleId);
        return articleInfoRepository.findOne(Example.of(info));
    }

    private ArticleOuter convertOuter(ArticleInfo info) {
        if (Objects.isNull(info)) {
            return null;
        }
        ArticleOuter outer = new ArticleOuter();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
