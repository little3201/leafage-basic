package top.abeille.basic.assets.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.abeille.basic.assets.entity.ArticleInfo;
import top.abeille.basic.assets.repository.ArticleInfoRepository;

/**
 * 文章接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Autowired
    private ArticleInfoRepository articleInfoRepository;

    @Test
    public void saveArticle() {
        ArticleInfo article = new ArticleInfo();
        article.setArticleId(2L);
        ArticleInfo result = articleInfoRepository.save(article);
        Assert.assertNotNull(result.getId());
    }

}
