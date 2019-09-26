/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.junit.Test;
import org.mockito.Mock;
import top.abeille.basic.assets.service.impl.ArticleInfoServiceImpl;
import top.abeille.common.mock.AbstractControllerMock;

/**
 * 文章管理接口测试类
 *
 * @author liwenqiang 2019/9/14 21:46
 **/
public class ArticleInfoControllerTest extends AbstractControllerMock {

    @Mock
    private ArticleInfoServiceImpl articleInfoService;

    @Override
    protected Object getController() {
        return new ArticleInfoController(articleInfoService);
    }

    @Test
    public void findArticles() {
        articleInfoService.findAll();
    }

    @Test
    public void getArticle() {
        articleInfoService.getById(0L);
    }
}
