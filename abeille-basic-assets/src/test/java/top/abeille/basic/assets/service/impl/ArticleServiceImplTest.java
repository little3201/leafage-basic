package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.service.ArticleService;
import top.abeille.basic.assets.vo.ArticleVO;

import java.util.Objects;

/**
 * 文章接口测试类
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@RunWith(SpringRunner.class)
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
        Assert.hasText(Objects.requireNonNull(outerMono.block()).getBusinessId(), "businessId must not be null");
    }

    @Test
    public void fetchById_returnObject() {
        Mono<? extends ArticleVO> outerMono = articleService.fetchByBusinessId("AT226");
        Assert.notNull(Objects.requireNonNull(outerMono.block()), "The class must not be null");
    }

    @Test
    public void fetchById_returnNull() {
        Mono<? extends ArticleVO> outerMono = articleService.fetchByBusinessId("AT226");
        Assert.isNull(Objects.requireNonNull(outerMono.block()).getBusinessId(), "The class must be null");
    }
}