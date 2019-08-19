/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.service;

import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.common.basic.BasicService;

/**
 * 文章信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface ArticleInfoService extends BasicService<ArticleInfo> {

    ArticleInfo getByArticleId(String articleId);

    void removeByArticleId(String articleId);
}
