package top.abeille.basic.assets.service.impl;

import org.junit.Assert;
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
        article.setArticleId("001");
        String content = "在前后端分离大行其道的今天，有一个统一的返回值格式不仅能使我们的接口看起来更漂亮，而且还可以使前端可以统一处理很多东西，避免很多问题的产生";
        article.setContent(content);
        Article result = articleRepository.save(article);
        Assert.assertNotNull(result.getContent());
    }
}
