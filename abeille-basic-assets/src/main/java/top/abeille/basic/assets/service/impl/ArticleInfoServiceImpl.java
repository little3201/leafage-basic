/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.ArticleInfo;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.ArticleVO;

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
    public Flux<ArticleVO> retrieveAll(Sort sort) {
        return articleInfoRepository.findAll(sort).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> fetchById(String articleId) {
        return fetchByArticleId(articleId).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> create(ArticleDTO articleDTO) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(articleDTO, info);
        return articleInfoRepository.save(info).map(this::convertOuter);
    }

    @Override
    public Mono<ArticleVO> modify(String articleId, ArticleDTO articleDTO) {
        return fetchByArticleId(articleId).flatMap(articleInfo -> {
            BeanUtils.copyProperties(articleDTO, articleInfo);
            return articleInfoRepository.save(articleInfo).map(this::convertOuter);
        });
    }

    @Override
    public Mono<Void> removeById(String articleId) {
        ArticleInfo info = new ArticleInfo();
        info.setArticleId(articleId);
        return articleInfoRepository.findOne(Example.of(info)).flatMap(article -> articleInfoRepository.deleteById(article.getId()));
    }

    /**
     * 根据ID查询
     *
     * @param articleId 文章ID
     * @return ArticleInfo 对象
     */
    private Mono<ArticleInfo> fetchByArticleId(String articleId) {
        if (Objects.isNull(articleId)) {
            return Mono.empty();
        }
        ArticleInfo info = new ArticleInfo();
        info.setArticleId(articleId);
        return articleInfoRepository.findOne(Example.of(info));
    }

    /**
     * 设置查询条件的必要参数
     *
     * @param info 信息
     * @return ArticleVO 输出对象
     */
    private ArticleVO convertOuter(ArticleInfo info) {
        ArticleVO outer = new ArticleVO();
        BeanUtils.copyProperties(info, outer);
        return outer;
    }
}
