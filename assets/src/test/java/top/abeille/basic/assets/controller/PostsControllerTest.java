/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.controller;

import org.junit.Test;
import org.mockito.Mock;
import top.abeille.basic.assets.service.impl.PostsServiceImpl;
import top.abeille.basic.assets.vo.PostsVO;
import top.abeille.common.mock.AbstractControllerMock;

import java.util.List;

/**
 * 文章管理接口测试类
 *
 * @author liwenqiang 2019/9/14 21:46
 **/
public class PostsControllerTest extends AbstractControllerMock<ArticleInfoController> {

    @Mock
    private PostsServiceImpl articleInfoService;

    @Override
    protected ArticleInfoController getController() {
        return new ArticleInfoController(articleInfoService);
    }

    @Test
    public void findArticles() {
        int page = 0;
        int size = 10;
        List<PostsVO> voList = articleInfoService.retrieve(page, size);
    }

}
