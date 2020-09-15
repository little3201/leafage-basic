/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.document.DetailsInfo;
import top.abeille.basic.assets.service.DetailsService;

/**
 * 内容接口测试类
 *
 * @author liwenqiang 2020/3/1 22:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DetailsServiceImplTest {

    @Autowired
    private DetailsService detailsService;

    @Test
    public void create() {
        DetailsInfo info = new DetailsInfo();
        info.setBusinessId("TP2277FZ0");
        info.setContent("Spring boot");
        Mono<DetailsInfo> mono = detailsService.create(info);
        Assert.notNull(mono.block(), "The class must not be null");
    }
}