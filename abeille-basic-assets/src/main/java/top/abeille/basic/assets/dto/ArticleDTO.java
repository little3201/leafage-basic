/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import java.io.Serializable;

/**
 * 用户信息入参
 *
 * @author liwenqiang  2019-03-03 22:59
 **/
public class ArticleDTO implements Serializable {

    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 标题
     */
    private String title;
    /**
     * 概览
     */
    private String summary;
    /**
     * 图片url
     */
    private String imageUrl;
    /**
     * 内容
     */
    private String content;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
