/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.impl.PostsServiceImpl;
import io.leafage.basic.assets.vo.PostsVO;
import io.leafage.common.mock.AbstractControllerMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;

/**
 * 文章管理接口测试类
 *
 * @author liwenqiang 2019/9/14 21:46
 **/
public class PostsControllerTest extends AbstractControllerMock<PostsController> {

    @Mock
    private PostsServiceImpl postsService;

    @Override
    protected PostsController getController() {
        return new PostsController(postsService);
    }

    @Test
    public void findArticles() {
        int page = 0;
        int size = 10;
        Page<PostsVO> voPage = postsService.retrieve(page, size, "");
    }

}
