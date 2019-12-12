package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.dto.ArticleDTO;
import top.abeille.basic.assets.service.ArticleInfoService;
import top.abeille.basic.assets.vo.ArticleVO;

/**
 * 文章接口实现类测试
 *
 * @author liwenqiang 2019/9/19 9:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Autowired
    private ArticleInfoService articleInfoService;

    @Test
    public void save() {
        ArticleDTO enter = new ArticleDTO();
        enter.setArticleId(20191009001L);
        enter.setTitle("spring");
        enter.setContent("spring boot 和 spring cloud");
        Mono<ArticleVO> outerMono = articleInfoService.create(null, enter);
    }
}