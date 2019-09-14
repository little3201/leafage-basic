package top.abeille.basic.assets.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 文章信息
 *
 * @author liwenqiang 2019-08-20 21:50
 **/
@Document(indexName = "article", type = "share")
public class Article {

    /**
     * 文章ID
     */
    @Id
    private String articleId;
    /**
     * 文章内容
     */
    private String details;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}