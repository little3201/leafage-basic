package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.abeille.basic.assets.document.Article;
import top.abeille.basic.assets.repository.ArticleRepository;

/**
 * 文章接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void getByArticleId() {
    }

    @Test
    public void saveArticle() {
        Article article = new Article();
        article.setArticleId("002");
        String content = "一步步带你实现web全景看房——three.js";
        article.setArticleContent(content);
        articleRepository.save(article);
    }
}
