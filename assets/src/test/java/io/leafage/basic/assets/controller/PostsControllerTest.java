/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.impl.PostsServiceImpl;
import io.leafage.basic.assets.vo.PostsVO;
import io.leafage.common.mock.AbstractControllerMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

/**
 * 文章管理接口测试类
 *
 * @author liwenqiang 2019/9/14 21:46
 **/
public class PostsControllerTest extends AbstractControllerMock<PostsController> {

    @Mock
    private PostsServiceImpl articleInfoService;

    @Override
    protected PostsController getController() {
        return new PostsController(articleInfoService);
    }

    @Test
    public void findArticles() {
        int page = 0;
        int size = 10;
        List<PostsVO> voList = articleInfoService.retrieve(page, size);
    }

}
