package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.ArticleVO;
import top.abeille.common.test.AbstractTest;

import java.util.Objects;

/**
 * 文章接口实现类测试
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@ExtendWith(AbstractTest.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Autowired
    private ArticleInfoService articleInfoService;

    @Test
    public void create() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("spring");
        articleDTO.setContent("spring boot 和 spring cloud");
        Mono<ArticleVO> outerMono = articleInfoService.create(articleDTO);
        Assert.hasText("Spring boot", Objects.requireNonNull(outerMono.block()).getContent());
    }

    @Test
    public void fetchById() {
    }
}