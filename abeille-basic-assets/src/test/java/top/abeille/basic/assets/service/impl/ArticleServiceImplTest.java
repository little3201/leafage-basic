package top.abeille.basic.assets.service.impl;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.service.ArticleService;
import top.abeille.basic.assets.vo.ArticleVO;

import java.util.Random;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@SpringBootTest
public class ArticleServiceImplTest {

    @Autowired
    private ArticleService articleService;

    @Test
    @Rollback
    public void create() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("spring");
        articleDTO.setContent("spring boot");
        Mono<ArticleVO> outerMono = articleService.create(articleDTO);
        Assert.notNull(outerMono.block(), "The class must not be null");
    }

    @Test
    public void fetchById_returnObject() {
        Mono<? extends ArticleVO> outerMono = articleService.fetchByCode("AT226");
        Assert.notNull(outerMono.block(), "The class must not be null");
    }

    @Test
    public void fetchById_returnNull() {
        Mono<? extends ArticleVO> outerMono = articleService.fetchByCode(String.valueOf(new Random().nextFloat()));
        Assert.isNull(outerMono.block(), "The class must be null");
    }
}