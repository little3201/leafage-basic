/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Enter class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostsDTO implements Serializable {

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
     * 封面
     */
    @NotBlank
    private String cover;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

}
