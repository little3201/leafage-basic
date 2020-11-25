package top.abeille.basic.assets.service.impl;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleInfoRepository;

/**
 * 文章接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Mock
    private ArticleInfoRepository articleInfoRepository;

    @Test
    public void saveArticle() {
        ArticleInfo article = new ArticleInfo();
        article.setBusinessId("");
        ArticleInfo result = articleInfoRepository.save(article);
        Assert.assertNotNull(result.getId());
    }

}
