/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for ContentInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "details")
public class Details extends BaseDocument {

    /**
     * 文章ID
     */
    @Field(value = "article_id")
    @Indexed(unique = true)
    private String articleId;
    /**
     * 原文
     */
    private String original;
    /**
     * 内容
     */
    private String content;
    /**
     * 目录
     */
    private String catalog;


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

}
