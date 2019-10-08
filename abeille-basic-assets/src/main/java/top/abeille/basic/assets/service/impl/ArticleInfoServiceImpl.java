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
        return articleInfoRepository.findAll().map(article -> {
            ArticleOuter outer = new ArticleOuter();
            BeanUtils.copyProperties(article, outer);
            return outer;
        });
    }

    @Override
    public Mono<ArticleOuter> getByArticleId(Long articleId) {
        ArticleInfo info = new ArticleInfo();
        info.setArticleId(articleId);
        return articleInfoRepository.findOne(Example.of(info)).map(article -> {
            ArticleOuter outer = new ArticleOuter();
            BeanUtils.copyProperties(article, outer);
            return outer;
        });
    }

    @Override
    public Mono<ArticleOuter> save(ArticleEnter enter) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(enter, info);
        return articleInfoRepository.save(info).map(article -> {
            ArticleOuter outer = new ArticleOuter();
            BeanUtils.copyProperties(article, outer);
            return outer;
        });
    }

    @Override
    public Mono<Void> removeById(String id) {
        return articleInfoRepository.deleteById(id);
    }
}
