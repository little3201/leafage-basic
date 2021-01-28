/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.PostsContent;
import io.leafage.basic.assets.service.PostsContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * 内容接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@SpringBootTest
public class PostsContentServiceImplTest {

    @Autowired
    private PostsContentService postsContentService;

    @Test
    public void create() {
        PostsContent info = new PostsContent();
        info.setContent("Spring boot");
        Mono<PostsContent> mono = postsContentService.create(info);
        Assert.notNull(mono.block(), "The class must not be null");
    }
}