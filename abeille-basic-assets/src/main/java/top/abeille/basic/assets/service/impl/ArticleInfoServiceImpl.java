/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.abeille.basic.assets.document.Article;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleInfoRepository;
import top.abeille.basic.assets.repository.ArticleRepository;
import top.abeille.basic.assets.service.ArticleInfoService;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

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
    private final ArticleRepository articleRepository;

    public ArticleInfoServiceImpl(ArticleInfoRepository articleInfoRepository, ArticleRepository articleRepository) {
        this.articleInfoRepository = articleInfoRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<ArticleInfo> findAllByPage(Integer pageNum, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher.withMatcher("is_enabled", exact());
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setEnabled(true);
        return articleInfoRepository.findAll(Example.of(articleInfo, exampleMatcher), pageable);
    }

    @Override
    public ArticleInfo getByExample(ArticleInfo articleInfo) {
        articleInfo.setEnabled(true);
        Optional<ArticleInfo> optional = articleInfoRepository.findOne(Example.of(articleInfo));
        return optional.orElse(null);
    }

    @Override
    public Article getByArticleId(String articleId) {
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setArticleId(articleId);
        Optional<Article> optional = articleRepository.findById(articleId);
        return optional.orElse(null);
    }

    @Override
    @Transactional
    public void removeById(Long id) {
        articleInfoRepository.deleteById(id);
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setId(id);
        ArticleInfo example = this.getByExample(articleInfo);
        if (null != example && StringUtils.isNotBlank(example.getArticleId())) {
            articleRepository.deleteById(example.getArticleId());
            log.info("remove article with articleId: {}, successful", example.getArticleId());
        }
    }

    @Override
    public void removeInBatch(List<ArticleInfo> entities) {
        articleInfoRepository.deleteInBatch(entities);
    }
}
