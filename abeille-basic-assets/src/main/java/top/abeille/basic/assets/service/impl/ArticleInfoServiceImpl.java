/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.service.ArticleInfoService;

import java.util.List;
import java.util.Optional;

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
    public ArticleInfo getByExample(ArticleInfo articleInfo) {
        articleInfo.setEnabled(true);
        Optional<ArticleInfo> optional = articleInfoRepository.findOne(Example.of(articleInfo));
        return optional.orElse(null);
    }

    @Override
    public ArticleInfo getByArticleId(String articleId) {
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setArticleId(articleId);
        return this.getByExample(articleInfo);

    }

    @Override
    public void removeByArticleId(String articleId) {

    }

    @Override
    public void removeById(Long id) {

    }

    @Override
    public void removeInBatch(List<ArticleInfo> entities) {

    }
}
