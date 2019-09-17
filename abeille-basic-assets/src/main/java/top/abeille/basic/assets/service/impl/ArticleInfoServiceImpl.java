/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;

import java.util.List;

/**
 * 文章信息service实现
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@Service
public class ArticleInfoServiceImpl implements ArticleInfoService {

    /**
     * 开启日志
     */
    private static final Logger log = LoggerFactory.getLogger(ArticleInfoServiceImpl.class);

    private final ArticleInfoRepository articleInfoRepository;

    public ArticleInfoServiceImpl(ArticleInfoRepository articleInfoRepository) {
        this.articleInfoRepository = articleInfoRepository;
    }

    @Override
    public Flux<ArticleInfo> findAll() {
        return articleInfoRepository.findAll();
    }

    @Override
    public Mono<ArticleInfo> getByExample(ArticleInfo articleInfo) {
        articleInfo.setEnabled(true);
        return articleInfoRepository.findOne(Example.of(articleInfo));
    }

    @Override
    public Mono<ArticleInfo> getByArticleId(String articleId) {
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setArticleId(articleId);
        return articleInfoRepository.findOne(Example.of(articleInfo));
    }

    @Override
    @Transactional
    public Mono<Void> removeById(Long id) {
        return articleInfoRepository.deleteById(id);
    }

    @Override
    public Mono<Void> removeInBatch(List<ArticleInfo> entities) {
        return articleInfoRepository.deleteAll(entities);
    }
}
