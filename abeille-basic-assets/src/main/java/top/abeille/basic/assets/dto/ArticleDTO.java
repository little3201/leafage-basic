/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Enter class for ArticleInfo
 *
 * @author liwenqiang
 */
public class ArticleDTO implements Serializable {

    private static final long serialVersionUID = 248576207213923230L;
    /**
     * 标题
     */
    @NotBlank
    private String title;
    /**
     * 描述
     */
    @NotBlank
    private String subtitle;
    /**
     * 内容
     */
    private String content;
    /**
     * 目录
     */
    private String catalog;
    /**
     * 图片url
     */
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
