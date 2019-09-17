/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.common.basic.BasicService;

/**
 * 文章信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface ArticleInfoService extends BasicService<ArticleInfo> {

    /**
     * 根据articleId从es中查询内容
     *
     * @param articleId 文章id
     * @return 文章内容
     */
    Mono<ArticleInfo> getByArticleId(String articleId);
}
